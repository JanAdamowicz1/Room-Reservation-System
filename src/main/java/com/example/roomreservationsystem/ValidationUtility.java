package com.example.roomreservationsystem;

public class ValidationUtility {

    private static ValidationUtility instance = null;
    public ValidationUtility() {

    }

    public static ValidationUtility getInstance() {
        if (instance == null) {
            instance = new ValidationUtility();
        }
        return instance;
    }

    public static boolean isNameValid(String customerName) {
        return customerName != null && customerName.matches("[a-zA-Z]+");
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("[0-9]{9}");
    }

}
