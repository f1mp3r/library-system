package app.models;

import app.utils.InputValidation;
import app.utils.QueryBuilder;
import app.utils.Screen;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class Users extends Model {

    public static String[] memberVisibleFields = new String[]{
            "@id as `#`",
            "@email as `E-Mail`",
            "@first_name as `First Name`",
            "@last_name as `Last Name`",
            "@student_id as `Student ID`",
            "@phone_number as `Phone Number`",
            "@CONCAT('£', debt) as `Fine`",
            "@IF(permission = 1, 'Administrator', 'User') as `User type`"
    };

    private static int id;
    private static String loggedInUserName;
    private static String loggedInStudentId;
    private static String loggedInStudentEmail;
    protected QueryBuilder queryBuilder = new QueryBuilder("users");

    public Users() {
        super();
        this.table = "users";
        this.hiddenFields.add("password");
    }

    public static String hash(String input) {
        String newHash = input;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            newHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            Screen.exception(e);
        }

        return newHash;
    }

    public static String getLoggedInStudentEmail() {
        return getLoggedInStudentId();
    }

    public static int getLoggedInUserTableID() {
        return id;
    }

    public static String getLoggedInUserName() {
        return loggedInUserName;
    }

    public static String getLoggedInStudentId() {
        return loggedInStudentId;
    }

    public boolean login(String studentId, String password, boolean staffOnly) {
        ResultSet resultSet;

        try {
            Connection connection = this.connection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `users` WHERE student_id = ? AND password = ? AND permission = ? ");
            statement.setString(1, studentId);
            statement.setString(2, Users.hash(password));
            statement.setBoolean(3, staffOnly);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = (Integer.parseInt(resultSet.getString("id")));
                loggedInUserName = resultSet.getString("first_name");
                loggedInStudentId = resultSet.getString("student_id");
                loggedInStudentEmail = resultSet.getString("email");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Screen.exception(e);
        }

        return false;
    }

    public boolean loginUser(String studentId, String password) {
        return this.login(studentId, password, false);
    }

    public boolean loginStaff(String studentId, String password) {
        return this.login(studentId, password, true);
    }

    public String getLoanedBooksQuery() {
        return this.queryBuilder
                .select(Loans.memberVisibleFields)
                .joinInner("loans", "users.id = loans.user_id")
                .joinInner("books", "books.id = loans.book_id")
                .where("id", "=", String.valueOf(Users.getLoggedInUserTableID()), "users")
                .where("returned", "=", "0", "loans")
                .build();
    }

    public int insert(HashMap data) {
        data.put("password", Users.hash(data.get("password").toString()));

        return super.insert(data);
    }

    /**
     * Data validation for new user
     * @param newUser
     * @return
     */
    public boolean validate(HashMap<String, String> newUser) {
        return InputValidation.isValidEmailAddress(newUser.get("email"))
                && InputValidation.isValidPassword(newUser.get("password"))
                && InputValidation.lengthCheck(newUser.get("student_id"))
                && InputValidation.lengthCheck(newUser.get("first_name"))
                && InputValidation.lengthCheck(newUser.get("last_name"))
                && InputValidation.lengthCheck(newUser.get("phone_number"))
                && InputValidation.isValidPhoneNumber(newUser.get("phone_number"))
                && InputValidation.validNameCheck(newUser.get("first_name"))
                && InputValidation.validNameCheck(newUser.get("last_name"))
                && InputValidation.lengthCheckForStudentId(newUser.get("student_id"));
    }
}
