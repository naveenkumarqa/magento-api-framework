package api.payload;

import api.utils.FakerUtils;

public class PayloadGenerator {

    private static String email;
    private static String password;
    private static String firstname;
    private static String lastname;

    public static void generateCustomerData() {
        email = FakerUtils.generateEmail();
        password = FakerUtils.generatePassword();
        firstname = FakerUtils.firstName();
        lastname = FakerUtils.lastName();
    }

    public static String getEmail() {
        return email;
    }

    public static String getPassword() {
        return password;
    }

    public static String getFirstname() {
        return firstname;
    }

    public static String getLastname() {
        return lastname;
    }

    public static void setEmail(String value) {
        email = value;
    }

    public static void setPassword(String value) {
        password = value;
    }

    public static void setFirstname(String value) {
        firstname = value;
    }

    public static void setLastname(String value) {
        lastname = value;
    }
}