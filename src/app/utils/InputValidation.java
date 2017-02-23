package app.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.soap.Text;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
            errorList.add("Invalid email");


        }
        return result;
    }


    public static boolean passwordValidation(String password) {


        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        boolean result = true;


        if (password.length() < 8) {
            errorList.add("Password length must be at least 8 characters"+ '\n');
            result = false;
        }else {

        }
        if (!UpperCasePatten.matcher(password).find()) {
            errorList.add("Password has to have at least one upper character" + '\n');
            result = false;

        }
        if (!digitCasePatten.matcher(password).find()) {
            errorList.add("Password has to have at least one digit"+ '\n');
            result = false;
        }

        return result;


    }

}