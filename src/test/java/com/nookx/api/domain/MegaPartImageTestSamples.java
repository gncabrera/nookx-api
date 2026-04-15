package com.nookx.api.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class MegaPartImageTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static MegaPartImage getMegaPartImageSample1() {
        return new MegaPartImage().id(1L);
    }

    public static MegaPartImage getMegaPartImageSample2() {
        return new MegaPartImage().id(2L);
    }

    public static MegaPartImage getMegaPartImageRandomSampleGenerator() {
        return new MegaPartImage().id(longCount.incrementAndGet());
    }
}
