package com.nookx.api.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MegaSetImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static MegaSetImage getMegaSetImageSample1() {
        return new MegaSetImage().id(1L);
    }

    public static MegaSetImage getMegaSetImageSample2() {
        return new MegaSetImage().id(2L);
    }

    public static MegaSetImage getMegaSetImageRandomSampleGenerator() {
        return new MegaSetImage().id(longCount.incrementAndGet());
    }
}
