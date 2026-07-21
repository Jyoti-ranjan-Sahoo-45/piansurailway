package com.railbookpro.util;

import java.security.SecureRandom;

public final class PnrGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PnrGenerator() {
    }

    // Indian railway PNRs are 10 digits
    public static String generate() {
        long number = 1_000_000_000L + (long) (RANDOM.nextDouble() * 8_999_999_999L);
        return String.valueOf(number);
    }
}
