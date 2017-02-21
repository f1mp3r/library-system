package app.models;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Chris on 21.2.2017 г..
 */
public class Users extends Model {
    public Users() {
        super();
        this.table = "users";
        this.hiddenFields.add("password");
    }

    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        String newHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return newHash;
    }

    public boolean login(String studentId, String password) {
        ResultSet resultSet;

        try {
            Connection connection = this.connection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `users` WHERE studentId = ? AND password = ? ");
            statement.setString(1, studentId);
            statement.setString(2, Users.hash(password));

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
}
