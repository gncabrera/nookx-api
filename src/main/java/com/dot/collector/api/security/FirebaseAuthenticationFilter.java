package com.dot.collector.api.security;

import com.dot.collector.api.domain.User;
import com.dot.collector.api.repository.UserRepository;
import com.dot.collector.api.service.FirebaseService;
import com.dot.collector.api.service.UserBuilderService;
import com.dot.collector.api.service.dto.AdminUserDTO;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseService firebaseService;
    private final UserBuilderService userBuilderService;
    private final UserRepository userRepository;

    public FirebaseAuthenticationFilter(
        FirebaseService firebaseService,
        UserBuilderService userBuilderService,
        UserRepository userRepository
    ) {
        this.firebaseService = firebaseService;
        this.userBuilderService = userBuilderService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = authHeader.substring(7);

        if (!firebaseService.isFirebaseIdToken(idToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            FirebaseToken firebaseToken = firebaseService.verifyIdToken(idToken);
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(firebaseToken));
            filterChain.doFilter(new AuthorizationStrippedRequest(request), response);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid or expired token.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal server error during authentication.");
        }
    }

    /**
     * Hides {@code Authorization} from downstream filters so the OAuth2 resource server does not try to decode
     * this Firebase RS256 token with the JHipster HS256 {@code JwtDecoder}.
     */
    private static final class AuthorizationStrippedRequest extends jakarta.servlet.http.HttpServletRequestWrapper {

        AuthorizationStrippedRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return null;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return Collections.emptyEnumeration();
            }
            return super.getHeaders(name);
        }
    }

    public Authentication getAuthentication(FirebaseToken token) {
        Optional<User> userOptional = userRepository.findOneWithAuthoritiesByEmailIgnoreCase(token.getEmail());

        User user = userOptional.orElseGet(() -> this.createUser(token));

        Collection<? extends GrantedAuthority> authorities = user
            .getAuthorities()
            .stream()
            .map(a -> new SimpleGrantedAuthority(a.getName()))
            .collect(Collectors.toList());

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            "",
            authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private User createUser(FirebaseToken token) {
        AdminUserDTO userDTO = new AdminUserDTO();
        userDTO.setAuthorities(new HashSet<>(Arrays.asList(AuthoritiesConstants.USER)));
        userDTO.setEmail(token.getEmail());
        userDTO.setLogin(token.getEmail());
        userDTO.setImageUrl(token.getPicture());
        return userBuilderService.createUser(userDTO);
    }
}
