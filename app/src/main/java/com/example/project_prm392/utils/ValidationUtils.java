package com.example.project_prm392.utils;

import android.text.TextUtils;
import android.util.Patterns;
import java.util.regex.Pattern;

public class ValidationUtils {

    // Email validation
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Password validation
    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= Constants.MIN_PASSWORD_LENGTH;
    }

    // Username validation
    public static boolean isValidUsername(String username) {
        return !TextUtils.isEmpty(username) &&
                username.length() >= Constants.MIN_USERNAME_LENGTH &&
                username.length() <= Constants.MAX_USERNAME_LENGTH;
    }

    // Full name validation
    public static boolean isValidFullName(String fullName) {
        return !TextUtils.isEmpty(fullName) && fullName.trim().length() >= 2;
    }

    // Phone validation (optional field)
    public static boolean isValidPhone(String phone) {
        // Phone is optional, so empty is valid
        if (TextUtils.isEmpty(phone)) return true;

        // Remove all spaces, dashes, and parentheses for validation
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");

        // Basic phone validation patterns
        Pattern phonePattern = Pattern.compile("^[+]?[0-9]{10,15}$");
        Pattern vietnamPhonePattern = Pattern.compile("^(\\+84|84|0)[1-9][0-9]{8,9}$");

        return phonePattern.matcher(cleanPhone).matches() ||
                vietnamPhonePattern.matcher(cleanPhone).matches();
    }

    // Address validation (optional field)
    public static boolean isValidAddress(String address) {
        // Address is optional, so empty is valid
        if (TextUtils.isEmpty(address)) return true;

        // Basic address validation - just check minimum length
        return address.trim().length() >= 5;
    }

    // Password strength validation
    public static boolean isStrongPassword(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 8) {
            return false;
        }

        // Check for at least one uppercase, one lowercase, one digit
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");

        return hasUppercase && hasLowercase && hasDigit;
    }

    // Check if passwords match
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(confirmPassword) &&
                password.equals(confirmPassword);
    }

    // Check if string contains only letters and spaces
    public static boolean isAlphaWithSpaces(String text) {
        if (TextUtils.isEmpty(text)) return false;
        return text.matches("^[a-zA-ZÀ-ÿ\\s]+$");
    }

    // Check if string contains only numbers
    public static boolean isNumeric(String text) {
        if (TextUtils.isEmpty(text)) return false;
        return text.matches("^[0-9]+$");
    }

    // Validate URL format
    public static boolean isValidUrl(String url) {
        return !TextUtils.isEmpty(url) && Patterns.WEB_URL.matcher(url).matches();
    }

    // Check minimum length
    public static boolean hasMinLength(String text, int minLength) {
        return !TextUtils.isEmpty(text) && text.length() >= minLength;
    }

    // Check maximum length
    public static boolean hasMaxLength(String text, int maxLength) {
        return TextUtils.isEmpty(text) || text.length() <= maxLength;
    }

    // Check if text is within length range
    public static boolean isLengthInRange(String text, int minLength, int maxLength) {
        return hasMinLength(text, minLength) && hasMaxLength(text, maxLength);
    }

    // Remove extra whitespaces and trim
    public static String sanitizeText(String text) {
        if (TextUtils.isEmpty(text)) return "";
        return text.trim().replaceAll("\\s+", " ");
    }

    // Validate Vietnamese phone number specifically
    public static boolean isValidVietnamesePhone(String phone) {
        if (TextUtils.isEmpty(phone)) return true; // Optional field

        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)]", "");

        // Vietnam phone patterns
        Pattern mobilePattern = Pattern.compile("^(\\+84|84|0)(3|5|7|8|9)[0-9]{8}$");
        Pattern landlinePattern = Pattern.compile("^(\\+84|84|0)(2)[0-9]{9,10}$");

        return mobilePattern.matcher(cleanPhone).matches() ||
                landlinePattern.matcher(cleanPhone).matches();
    }

    // Email domain validation
    public static boolean isValidEmailDomain(String email, String[] allowedDomains) {
        if (!isValidEmail(email)) return false;

        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        for (String allowedDomain : allowedDomains) {
            if (domain.equals(allowedDomain.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // Check if text contains special characters
    public static boolean containsSpecialCharacters(String text) {
        if (TextUtils.isEmpty(text)) return false;
        Pattern specialChars = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");
        return specialChars.matcher(text).find();
    }

    // Username format validation (alphanumeric and underscore only)
    public static boolean isValidUsernameFormat(String username) {
        if (TextUtils.isEmpty(username)) return false;
        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        return usernamePattern.matcher(username).matches();
    }

    // Check if string contains only whitespace
    public static boolean isOnlyWhitespace(String text) {
        return !TextUtils.isEmpty(text) && text.trim().isEmpty();
    }

    // Validate age (if needed for user registration)
    public static boolean isValidAge(int age) {
        return age >= 13 && age <= 120; // Reasonable age range
    }

    // Format phone number for display
    public static String formatPhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) return "";

        String cleanPhone = phone.replaceAll("[^0-9+]", "");

        // Vietnam mobile format: 0xxx xxx xxx
        if (cleanPhone.startsWith("0") && cleanPhone.length() == 10) {
            return cleanPhone.substring(0, 4) + " " +
                    cleanPhone.substring(4, 7) + " " +
                    cleanPhone.substring(7);
        }

        return phone; // Return original if can't format
    }

    // Get validation error message for field
    public static String getValidationError(String fieldName, String value, ValidationRule rule) {
        switch (rule) {
            case REQUIRED:
                return TextUtils.isEmpty(value) ? fieldName + " is required" : null;
            case EMAIL:
                return !isValidEmail(value) ? "Please enter a valid email address" : null;
            case PASSWORD:
                return !isValidPassword(value) ?
                        "Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters" : null;
            case PHONE:
                return !isValidPhone(value) ? "Please enter a valid phone number" : null;
            case USERNAME:
                return !isValidUsername(value) ?
                        "Username must be " + Constants.MIN_USERNAME_LENGTH + "-" +
                                Constants.MAX_USERNAME_LENGTH + " characters" : null;
            default:
                return null;
        }
    }

    // Validation rules enum
    public enum ValidationRule {
        REQUIRED,
        EMAIL,
        PASSWORD,
        PHONE,
        USERNAME,
        FULL_NAME
    }
}