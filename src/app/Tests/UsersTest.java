package app.Tests;

import app.models.Users;
import junit.framework.TestCase;

import java.util.HashMap;

/**
 * Created by Thez on 3/28/2017.
 */
public class UsersTest extends TestCase {
    Users users;

    @Override
    public void setUp() {
        this.users = new Users();
    }

    public void testLoginUser() throws Exception {

        HashMap newUser = new HashMap();
        newUser.put("student_id", "user");
        newUser.put("password", "user");
        newUser.put("first_Name", "user");
        newUser.put("last_Name", "test44");
        newUser.put("phone_number", "test55");
        newUser.put("email", "user6@test.test");
        newUser.put("permission", 0);
        this.users.insert(newUser);

        HashMap newUserInvalid = new HashMap();
        newUserInvalid.put("student_id", "admin");
        newUserInvalid.put("password", "admin");
        newUserInvalid.put("first_Name", "notuser");
        newUserInvalid.put("last_Name", "test44");
        newUserInvalid.put("phone_number", "test55");
        newUserInvalid.put("email", "admin@test.test");
        newUserInvalid.put("permission", 1);
        this.users.insert(newUserInvalid);


        boolean valid = this.users.login("user", "user", false);
        boolean invalid = this.users.login("notuser", "notuser", false);
        boolean admin = this.users.login("admin", "admin", false);

        assertEquals(true, valid);
        assertEquals(false, invalid);
        assertEquals(false, admin);
    }

    public void testLoginStaff() throws Exception {
        HashMap notadmin = new HashMap();
        notadmin.put("student_id", "user2");
        notadmin.put("password", "user2");
        notadmin.put("first_Name", "user2");
        notadmin.put("last_Name", "test44");
        notadmin.put("phone_number", "test55");
        notadmin.put("email", "user2@test.test");
        notadmin.put("permission", 0);
        this.users.insert(notadmin);

        HashMap newAdmin = new HashMap();
        newAdmin.put("student_id", "admin2");
        newAdmin.put("password", "admin2");
        newAdmin.put("first_Name", "notuser2");
        newAdmin.put("last_Name", "test44");
        newAdmin.put("phone_number", "test55");
        newAdmin.put("email", "admi2n@test.test");
        newAdmin.put("permission", 1);

        this.users.insert(newAdmin);

        boolean admin = this.users.loginStaff("admin2", "admin2");
        boolean user = this.users.loginStaff("user2", "user2");

        assertEquals(true, admin);
        assertEquals(false, user);
    }

    @Override
    public void tearDown() {
        // delete the test data
        String[] testData = new String[7];
        testData[0] = "test66@test.test";
        testData[1] = "test666@test.test";
        testData[2] = "testy@test.edited";
        testData[3] = "user6@test.test";
        testData[4] = "admin@test.test";
        testData[5] = "user2@test.test";
        testData[6] = "admi2n@test.test";

        for (String email : testData) {
            this.users.delete(new HashMap() {{
                put("email", email);
            }});
        }
    }
}