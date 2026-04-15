package com.nookx.api.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ProfileCollectionImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static ProfileCollectionImage getProfileCollectionImageSample1() {
        return new ProfileCollectionImage().id(1L);
    }

    public static ProfileCollectionImage getProfileCollectionImageSample2() {
        return new ProfileCollectionImage().id(2L);
    }

    public static ProfileCollectionImage getProfileCollectionImageRandomSampleGenerator() {
        return new ProfileCollectionImage().id(longCount.incrementAndGet());
    }
}
