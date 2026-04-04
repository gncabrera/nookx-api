package com.dot.collector.api.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {
        try {
            // Verify the ID token
            // The method verifyIdToken(idToken) does not check for revocation by default.
            // Use verifyIdToken(idToken, true) to check for revocation.
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            System.out.println("Successfully verified ID token for UID: " + decodedToken.getUid());
            return decodedToken;
        } catch (FirebaseAuthException e) {
            System.err.println("Error verifying ID token: " + e.getMessage());
            throw e; // Re-throw the exception for further handling in your application
        }
    }

    public boolean isFirebaseIdToken(String token) {
        try {
            String iss = SignedJWT.parse(token).getJWTClaimsSet().getIssuer();
            return iss != null && iss.startsWith("https://securetoken.google.com/");
        } catch (ParseException e) {
            return false;
        }
    }
}
