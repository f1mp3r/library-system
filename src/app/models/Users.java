package app.models;

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
            System.out.println(e.getMessage());
        }

        return newHash;
    }

    public boolean login(String studentId, String password, boolean staffOnly) {
        ResultSet resultSet;

        try {
            Connection connection = this.connection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `users` WHERE student_Id = ? AND password = ? AND permission = ? ");
            statement.setString(1, studentId);
            statement.setString(2, Users.hash(password));
            statement.setBoolean(3, staffOnly);

            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }

        return false;
    }

    public boolean loginUser(String studentId, String password) {
        return this.login(studentId, password, false);
    }

    public boolean loginStaff(String studentId, String password) {
        return this.login(studentId, password, true);
    }

    public int insert(HashMap data) {
        data.put("password", Users.hash(data.get("password").toString()));

        return super.insert(data);
    }
}
