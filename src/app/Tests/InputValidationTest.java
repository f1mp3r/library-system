package app.Tests;

import app.utils.InputValidation;
import junit.framework.TestCase;

import static org.junit.Assert.assertEquals;

/**
 * Created by Thez on 3/28/2017.
 */
public class InputValidationTest extends TestCase {
    public void testIsValidEmailAddress() throws Exception {

            boolean email=  InputValidation.isValidEmailAddress("realemail@real.com");
            boolean emailfake = InputValidation. isValidEmailAddress("notemail");

            assertEquals(true,email);
            assertEquals(false, emailfake);

    }

    public void testIsValidPassword() throws Exception {
        //has to have one Capital letter and 1 number and be 8 characters
        boolean passwordGood = InputValidation.isValidPassword("Testy123");

        //no capital letter
        boolean passwordBad1 = InputValidation.isValidPassword("test123");

        //no numbers
        boolean passwordBad2 = InputValidation.isValidPassword("Test");

        //no numbers or capital letters
        boolean passwordBad3 = InputValidation.isValidPassword("testforpassword");

        //no numbers or capital letters and not atleast 8 characters long
        boolean passwordBad4 = InputValidation.isValidPassword("test");

        assertEquals(true, passwordGood);
        assertEquals(false, passwordBad1);
        assertEquals(false, passwordBad2);
        assertEquals(false, passwordBad4);
    }

    public void testLengthCheck() throws Exception {
        //string not empty
        boolean isNotEmpty = InputValidation.lengthCheck("test");
        //string empty
        boolean isEmpty = InputValidation.lengthCheck("");

        assertEquals(true, isNotEmpty);
        assertEquals(false, isEmpty);

    }

    public void testLengthCheckForStudentId() throws Exception {
        //id has to not exceed 10 characters
        boolean is10 = InputValidation.lengthCheckForStudentId("1234567891");
        boolean is11 = InputValidation.lengthCheckForStudentId("12345678910");

        assertEquals(true, is10);
        assertEquals(false,is11);

    }

    public void testValidNameCheck() throws Exception {
        //must only contain letters
        boolean isValidName = InputValidation.validNameCheck("dsada");

        boolean isNot = InputValidation.validNameCheck("2341");

        assertEquals(true, isValidName);
        assertEquals(false,isNot);

    }

}