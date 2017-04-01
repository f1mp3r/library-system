package app.utils;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thez on 2/23/2017.
 */
public class InputValidation {


    public static ArrayList<String> errorList = new ArrayList<>();

    public static ArrayList<String> getErrorList() {
        return errorList;
    }

    public static boolean isValidEmailAddress(String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();

            return true;
        } catch (AddressException ex) {
            errorList.add("Invalid email");

            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        Pattern UpperCasePatten = Pattern.compile("[A-Z]");
        Pattern digitCasePatten = Pattern.compile("[0-9]");

        boolean result = true;

        if (password.length() < 8) {
            errorList.add("Password length must be at least 8 characters" + '\n');
            result = false;
        }

        if (!UpperCasePatten.matcher(password).find()) {
            errorList.add("Password has to have at least one upper character" + '\n');
            result = false;

        }
        if (!digitCasePatten.matcher(password).find()) {
            errorList.add("Password has to have at least one digit" + '\n');
            result = false;
        }

        return result;
    }

    public static boolean lengthCheck(String wordToCheck) {
        boolean result = true;

        if (wordToCheck.length() <= 0) {
            errorList.add("You missed an entry" + '\n');
            result = false;
        }

        return result;
    }

    public static boolean lengthCheckForStudentId(String wordToCheck) {
        boolean result = true;

        if (wordToCheck.length() > 10) {
            errorList.add("Student Id can only be 10 characters long" + '\n');
            result = false;
        }

        return result;
    }

    public static boolean validNameCheck(String wordToCheck) {
        boolean allLetters = wordToCheck.chars().allMatch(Character::isLetter);

        if (!allLetters) {
            errorList.add("Name fields must only contain letters" + '\n');
        }

        return allLetters;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        boolean allLetters = phoneNumber.chars().allMatch(Character::isDigit);

        if (!allLetters) {
            errorList.add("Phone fields must only contain numbers" + '\n');
        }

        return allLetters;
    }
}