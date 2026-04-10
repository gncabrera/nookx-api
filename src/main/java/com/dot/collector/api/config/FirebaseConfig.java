package com.dot.collector.api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LogManager.getLogger(FirebaseConfig.class.getName());

    @Value("classpath:firebase_config.json")
    private Resource firebase_config;

    @PostConstruct
    public void init() {
        try (InputStream serviceAccount = Files.newInputStream(firebase_config.getFile().toPath())) {
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

            if (FirebaseApp.getApps().isEmpty()) {
                // Check if FirebaseApp is already initialized
                FirebaseApp.initializeApp(options);
            }
            logger.info("Firebase Admin SDK initialized successfully.");
        } catch (IOException e) {
            logger.error("Error initializing Firebase Admin SDK: {}", e.getMessage(), e);
        }
    }
}
