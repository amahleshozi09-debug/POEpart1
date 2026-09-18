import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class LoginTest {

    // Test valid username
    @Test
    public void testCheckUserName_Valid() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkUserName());
    }

    // Test invalid username
    @Test
    public void testCheckUserName_Invalid() {

        Login user = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkUserName());
    }

    // Test valid password
    @Test
    public void testCheckPasswordComplexity_Valid() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkPasswordComplexity());
    }

    // Test invalid password
    @Test
    public void testCheckPasswordComplexity_Invalid() {

        Login user = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkPasswordComplexity());
    }

    // Test valid cellphone number
    @Test
    public void testCheckCellPhoneNumber_Valid() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(user.checkCellPhoneNumber());
    }

    // Test invalid cellphone number
    @Test
    public void testCheckCellPhoneNumber_Invalid() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        assertFalse(user.checkCellPhoneNumber());
    }

    // Test successful login
    @Test
    public void testLoginUser_Successful() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertTrue(
                user.loginUser("kyl_1", "Ch&&sec@ke99!")
        );
    }

    // Test failed login
    @Test
    public void testLoginUser_Failed() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertFalse(
                user.loginUser("wrong", "wrong")
        );
    }

    // Test successful registration
    @Test
    public void testRegisterUser_Successful() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "User has been registered successfully.",
                user.registerUser()
        );
    }

    // Test registration with invalid username
    @Test
    public void testRegisterUser_InvalidUsername() {

        Login user = new Login(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Username is not correctly formatted; please ensure "
                + "that your username contains an underscore and is "
                + "no more than five characters in length.",
                user.registerUser()
        );
    }

    // Test registration with invalid password
    @Test
    public void testRegisterUser_InvalidPassword() {

        Login user = new Login(
                "kyl_1",
                "password",
                "+27838968976",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Password is not correctly formatted; please ensure "
                + "that the password contains at least eight characters, "
                + "a capital letter, a number, and a special character.",
                user.registerUser()
        );
    }

    // Test registration with invalid cellphone number
    @Test
    public void testRegisterUser_InvalidCellPhone() {

        Login user = new Login(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Kyle",
                "Smith"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not "
                + "contain an international code.",
                user.registerUser()
        );
    }
}