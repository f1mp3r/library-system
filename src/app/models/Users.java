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
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `users` WHERE student_Id = ? AND password = ? ");
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


    public void addUser(String addstudentID, String addpassword, String addfirstName, String addlastName, String addphoneNumber,
                        String addemail, boolean addpermission) {
        try {


            Users users = new Users();
            HashMap data = new HashMap();


            data.put("email", addemail);
            data.put("first_name", addfirstName);
            data.put("last_name", addlastName);
            data.put("student_id", addstudentID);
            data.put("password", Users.hash(addpassword));
            data.put("permission", addpermission);
            data.put("phone_number",addphoneNumber);
            users.insert(data);


        } catch(Exception exc) {

        }
    }
}
