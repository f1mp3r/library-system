package app.Tests;

import app.models.Users;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by Thez on 3/28/2017.
 */
public class ModelTest extends TestCase {
    Users users = new Users();

    @Test
    public void addUserForTesting() throws Exception {

        HashMap newUser = new HashMap();
        newUser.put("student_id", "test1");
        newUser.put("password", "test2");
        newUser.put("first_Name", "test3");
        newUser.put("last_Name", "test4");
        newUser.put("phone_number", "test5");
        newUser.put("email", "test@test.test");
        newUser.put("permission", 0);
        users.insert(newUser);
        HashMap data = users.getByColumn("student_id", "test1");
        assertEquals("test1", data.get("student_id"));
        assertEquals("test3", data.get("first_name"));
        assertEquals("test4", data.get("last_name"));
        assertEquals("test5", data.get("phone_number"));
        assertEquals("test@test.test", data.get("email"));
        assertEquals("0", data.get("permission"));
    }

    @Test
    public void testGetByColumn() throws Exception {
        HashMap newUser = new HashMap();
        newUser.put("student_id", "test11");
        newUser.put("password", "test22");
        newUser.put("first_Name", "test33");
        newUser.put("last_Name", "test44");
        newUser.put("phone_number", "test55");
        newUser.put("email", "test66@test.test");
        newUser.put("permission", 0);
        users.insert(newUser);

        HashMap data = users.getByColumn("student_id", "test11");
        assertEquals("test11", data.get("student_id"));
        assertEquals("test33", data.get("first_name"));
        assertEquals("test44", data.get("last_name"));
        assertEquals("test55", data.get("phone_number"));
        assertEquals("test66@test.test", data.get("email"));
        assertEquals("0", data.get("permission"));
    }

    @Test
    public void testGetByID() throws Exception {

        HashMap newUser = new HashMap();
        newUser.put("student_id", "test111");
        newUser.put("password", "test222");
        newUser.put("first_Name", "test333");
        newUser.put("last_Name", "test444");
        newUser.put("phone_number", "test555");
        newUser.put("email", "test666@test.test");
        newUser.put("permission", 0);
        users.insert(newUser);

        HashMap dataToGetID = users.getByColumn("student_id", "test111");
        int id = Integer.parseInt(dataToGetID.get("id").toString());
        HashMap data = users.getById(id);
        assertEquals("test111", data.get("student_id"));
        assertEquals("test333", data.get("first_name"));
        assertEquals("test444", data.get("last_name"));
        assertEquals("test555", data.get("phone_number"));
        assertEquals("test666@test.test", data.get("email"));
        assertEquals("0", data.get("permission"));

    }


    @Test
    public void testUpdate() throws Exception {
        HashMap newUser = new HashMap();
        newUser.put("student_id", "testy");
        newUser.put("password", "testy");
        newUser.put("first_Name", "testy");
        newUser.put("last_Name", "testy");
        newUser.put("phone_number", "testy");
        newUser.put("email", "testy@test.test");
        newUser.put("permission", 0);
        users.insert(newUser);
        HashMap dataToGetID = users.getByColumn("student_id", "testy");
        int id = Integer.parseInt(dataToGetID.get("id").toString());

        HashMap updateUser = new HashMap();
        updateUser.put("student_id", "'testyedit'");
        updateUser.put("last_Name", "'testyedited'");
        updateUser.put("first_Name", "'testyedited'");
        updateUser.put("phone_number", "'testyedited'");
        updateUser.put("email", "'testy@test.edited'");
        updateUser.put("permission", 1);
        users.update(updateUser, id);


        HashMap data2 = users.getByColumn("student_id", "testyedit");
        assertEquals(("testyedit"), data2.get("student_id"));
        assertEquals(("testyedited"), data2.get("first_name"));
        assertEquals(("testyedited"), data2.get("last_name"));
        assertEquals(("testyedited"), data2.get("phone_number"));
        assertEquals(("testy@test.edited"), data2.get("email"));
        assertEquals(("1"), data2.get("permission"));


    }

}