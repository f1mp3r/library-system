package app;

import java.sql.*;


/**
 * Created by Ed on 2/17/17.
 */
public class UserMethods {
    private String studentID;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String email;
    private boolean permission;


    public UserMethods() {

    }


    public Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/users";
        String user = "root";
        String serverpassword = "root";
        Connection myConn;

        try {
            myConn = DriverManager.getConnection(url, user, serverpassword);
            return myConn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void addUser(String addstudentID, String addpassword, String addfirstName, String addlastName, String addphoneNumber,
                        String addemail, boolean addpermission) {
        try {

            Connection myConn;
            myConn = getConnection();


            String addUser = "insert into users" + "(studentId, password, firstname, lastname, phonenumber," +
                    " email, permission)" + "values (?, ?, ?, ?, ?, ?, 0)";


            PreparedStatement prepStmt = myConn.prepareStatement(addUser);
            prepStmt.setString(1, addstudentID);
            prepStmt.setString(2, addpassword);
            prepStmt.setString(3, addfirstName);
            prepStmt.setString(4, addlastName);
            prepStmt.setString(5, addphoneNumber);
            prepStmt.setString(6, addemail);


            prepStmt.execute();
            System.out.print("Insert complete");
            myConn.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    public boolean login(String username, String password) {
        ResultSet rs;
        try {
            Connection myConn = getConnection();
            PreparedStatement prepstmt = myConn
                    .prepareStatement("SELECT * FROM users where studentId = ? AND password = ? ");
            prepstmt.setString(1, username);
            prepstmt.setString(2, password);


            rs = prepstmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }


        } catch (Exception exc) {
            exc.printStackTrace();
            return false;
        }
    }


}



