import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class LoginUserTest {

    UserClient userClient = new UserClient();
    UserOperation userOperation = new UserOperation();
    String token;


    @After
    public void clearData() {
        if (token != null) {
            ValidatableResponse deleteResponse = userClient.deleteUser(token);
            deleteResponse.statusCode(202);
        }
    }

    @DisplayName("Логин ранее зарегестрированного пользователя")
    @Test
    public void loginExistingUser() {
        Map<String, String> responseData = userOperation.registerUserAndGetData();

        User user = new UserBuilder()
                .setEmail(responseData.get("email"))
                .setPassword(responseData.get("password"))
                .build();

        ValidatableResponse response = userClient.loginUser(user);
        token = response.extract().path("accessToken");
        response.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @DisplayName("Логин с неверным паролеем и email")
    @Test
    public void loginInvalidPassAndEmail() {
        User user = new UserBuilder()
                .setRandomEmail()
                .setRandomPassword()
                .build();

        ValidatableResponse response = userClient.loginUser(user);
        response.assertThat().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}
