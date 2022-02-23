import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.After;
import static org.hamcrest.Matchers.equalTo;

import io.qameta.allure.junit4.DisplayName;

import java.util.Map;

public class ChangingUserDataTest {

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

    @DisplayName("Обновление Email у авторизованного пользователя")
    @Test
    public void changingEmailAuthorizedUser() {
        Map<String, String> responseData = userOperation.registerUserAndGetData();
        token = responseData.get("token");

        User existingUser = new UserBuilder()
                .setRandomEmail().build();
        ValidatableResponse responseExistingUser = userClient.changingUserData(responseData.get("token"), existingUser);
        responseExistingUser.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @DisplayName("Обновление имени у авторизованного пользователя")
    @Test
    public void changingNameAuthorizedUser() {
        Map<String, String> responseData = userOperation.registerUserAndGetData();
        token = responseData.get("token");

        User existingUser = new UserBuilder().setRandomName().build();
        ValidatableResponse responseExistingUser = userClient.changingUserData(responseData.get("token"), existingUser);
         responseExistingUser.assertThat().body("success", equalTo(true))
                 .and()
                 .statusCode(200);
    }

    @DisplayName("Обновление пароля у авторизованного пользователя")
    @Test
    public void changingPasswordAuthorizedUser() {
        Map<String, String> responseData = userOperation.registerUserAndGetData();
        token = responseData.get("token");

        User existingUser = new UserBuilder().setRandomPassword().build();
        ValidatableResponse responseExistingUser = userClient.changingUserData(responseData.get("token"), existingUser);
        responseExistingUser.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @DisplayName("Обновление Email у пользователя без авторизации")
    @Test
    public void changingEmailNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomEmail().build();
        ValidatableResponse response = userClient.changingUserData(null, existingUser);
        response.assertThat().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @DisplayName("Обновление имени у пользователя без авторизации")
    @Test
    public void changingNameNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomName().build();
        ValidatableResponse response = userClient.changingUserData(null, existingUser);
        response.assertThat().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @DisplayName("Обновление пароля у пользователя без авторизации")
    @Test
    public void changingPasswordNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomName().build();
        ValidatableResponse response = userClient.changingUserData(null, existingUser);
        response.assertThat().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
