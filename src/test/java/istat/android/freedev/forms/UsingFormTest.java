package istat.android.freedev.forms;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import istat.android.freedev.forms.utils.ClassFormLoader;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UsingFormTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Form form = new Form();
        form.put("userName", "papa");
        form.put("firstName", "Jephte");
        form.put("year", 45);
        User user = new User();//form.as(User.class);
        form.flowInto(user);
        assertEquals("papa", user.getName());
        assertEquals("Jephte", user.getFirstName());
        assertEquals(45, user.getYear());
//        ClassFormLoader.putAsFormLoader(userFormLoader);
//        ClassFormLoader.putAsObjectLoader(userEntityLoader);
        user = form.as(User.class);
        assertEquals("papa", user.getName());
        assertEquals("Jephte", user.getFirstName());
        assertEquals(45, user.getYear());

        Form form2 = Form.createFrom(user);
        assertEquals(form2.get("userName"), user.getName());
        assertEquals(form2.get("firstName"), user.getFirstName());
        assertEquals(form2.get("year"), user.getYear());
    }

    ClassFormLoader<User> userEntityLoader = new ClassFormLoader<User>() {
        @Override
        public void onLoad(Form form, User entity) {
            entity.firstName = "Yelllo";
            entity.userName = "salmonel";
            entity.year = 100;

        }

        public void onLoad2(Form form, User entity) {
            entity.firstName = "Yelllo";
            entity.userName = "salmonel";
            entity.year = 100;

        }
    };
    ClassFormLoader<User> userFormLoader = new ClassFormLoader<User>() {
        @Override
        public void onLoad(Form form, User entity) {
            form.put("userName", "okGoogle");
            form.put("firstName", "polipop");
            form.put("year", "jolinark");
        }
    };

    public class User {
        public String userName;
        public String firstName;
        public int year;

        boolean readOnly = false;

        public int getYear() {
            return year;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getName() {
            return userName;
        }

        @Override
        public String toString() {
            try {
                return toJson().toString(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return super.toString();
        }

        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            try {
                json.put("userName", userName);
                json.put("firstname", firstName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
    }

}