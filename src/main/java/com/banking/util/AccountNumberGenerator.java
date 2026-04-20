package com.banking.util;

import java.util.concurrent.ThreadLocalRandom;

public final class AccountNumberGenerator {

    private AccountNumberGenerator() {}

    public static String generate() {
        long number = ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        return "ACC" + number;
    }
}