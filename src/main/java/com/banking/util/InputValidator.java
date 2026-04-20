package com.banking.util;

import java.math.BigDecimal;

public final class InputValidator {

    private InputValidator() {}

    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{4,50}$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8
                && password.chars().anyMatch(Character::isDigit)
                && password.chars().anyMatch(Character::isUpperCase);
    }
}