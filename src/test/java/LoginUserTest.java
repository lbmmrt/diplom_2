import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LoginUserTest {

    UserClient userClient= new UserClient();
    UserOperation userOperation = new UserOperation();
    String token;


    @After
    public void clearData() {
        if (token != null) {
            ValidatableResponse deleteResponse = userClient.deleteUser(token);
            deleteResponse.statusCode(202);
        }
    }

    @Test
    public void loginExistingUser() {
        Map<String, String> responseData = userOperation.registerUser();

        User user = new UserBuilder()
                .setEmail(responseData.get("email"))
                .setPassword(responseData.get("password"))
                .build();

        ValidatableResponse response = userClient.loginUser(user);
        response.assertThat().statusCode(200);
        boolean isLoginExistingUser = response.extract().path("success");
        Assert.assertTrue("User is not login", isLoginExistingUser);

        token = response.extract().path("accessToken");
    }

    @Test
    public void loginInvalidPassAndEmail() {
        User user = new UserBuilder()
                .setRandomEmail()
                .setRandomPassword()
                .build();

        ValidatableResponse response = userClient.loginUser(user);
        response.assertThat().statusCode(401);
        boolean isLoginExistingUser = response.extract().path("success");
        Assert.assertFalse("User is not login", isLoginExistingUser);

        String errorMessage = response.extract().path("message");
        assertEquals("email or password are incorrect", errorMessage);
    }
}
