import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RegisterUserTest {

    private UserClient userClient;
    UserOperation userOperation = new UserOperation();
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void clearData() {
        if (token != null) {
            ValidatableResponse deleteResponse = userClient.deleteUser(token);
            deleteResponse.statusCode(202);
        }
    }

    @Test
    public void registerUser() {
        Map<String, String> responseData = userOperation.registerUser();

        token = responseData.get("token");
    }

    @Test
    public void registeringAnExistingUser() {
        Map<String, String> responseData = userOperation.registerUser();

        User existingUser = new UserBuilder()
                .setEmail(responseData.get("email"))
                .setPassword(responseData.get("password"))
                .setName(responseData.get("name"))
                .build();
        ValidatableResponse responseExistingUser = userClient.registerUser(existingUser);
        responseExistingUser.assertThat().statusCode(403);
        boolean isRegisteredExistingUser = responseExistingUser.extract().path("success");
        Assert.assertFalse("User is registered", isRegisteredExistingUser);
        String errorMessage = responseExistingUser.extract().path("message");
        assertEquals("User already exists", errorMessage);

        token = responseData.get("token");
    }

    @Test
    public void registeringWithoutName() {
        User newUser = new UserBuilder()
                .setRandomEmail()
                .setRandomPassword()
                .build();
        ValidatableResponse response = userClient.registerUser(newUser);
        response.assertThat().statusCode(403);

        boolean isRegistered = response.extract().path("success");
        Assert.assertFalse("User is registered", isRegistered);

        String errorMessage = response.extract().path("message");
        assertEquals("Email, password and name are required fields", errorMessage);
    }
}
