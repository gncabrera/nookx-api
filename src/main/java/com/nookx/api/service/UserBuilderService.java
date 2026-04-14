package com.nookx.api.service;

import com.github.javafaker.Faker;
import com.nookx.api.domain.Authority;
import com.nookx.api.domain.Profile;
import com.nookx.api.domain.ProfileCollection;
import com.nookx.api.domain.User;
import com.nookx.api.domain.enumeration.ProfileCollectionType;
import com.nookx.api.repository.AuthorityRepository;
import com.nookx.api.repository.ProfileCollectionRepository;
import com.nookx.api.repository.ProfileRepository;
import com.nookx.api.repository.UserRepository;
import com.nookx.api.security.AuthoritiesConstants;
import com.nookx.api.service.dto.AdminUserDTO;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.jhipster.security.RandomUtil;

@Service
public class UserBuilderService {

    private static final Logger LOG = LoggerFactory.getLogger(UserBuilderService.class);

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final ProfileCollectionRepository profileCollectionRepository;

    public UserBuilderService(
        AuthorityRepository authorityRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        ProfileRepository profileRepository,
        ProfileCollectionRepository profileCollectionRepository
    ) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.profileRepository = profileRepository;
        this.profileCollectionRepository = profileCollectionRepository;
    }

    public User createUser(AdminUserDTO userDTO) {
        return createUser(userDTO, "el pepe de la gente");
    }

    public User createUser(AdminUserDTO userDTO, String password) {
        User user = this.buildUser(userDTO, password);
        user = userRepository.save(user);
        //this.clearUserCaches(user);

        createUserProfile(user);
        return user;
    }

    private User buildUser(AdminUserDTO userDTO, String password) {
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        LOG.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public Profile createUserProfile(User user) {
        Faker faker = new Faker();
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setUsername(faker.funnyName().name());
        profile.setFullName(faker.name().fullName());
        profile = profileRepository.save(profile);

        ProfileCollection profileCollection = new ProfileCollection();
        profileCollection.setProfile(profile);
        profileCollection.setType(ProfileCollectionType.STANDARD);
        profileCollection.setIsPublic(false);
        profileCollection.setTitle("Default");
        profileCollection.setDescription("Default Collection");
        profileCollection = profileCollectionRepository.save(profileCollection);
        return profile;
    }
}
