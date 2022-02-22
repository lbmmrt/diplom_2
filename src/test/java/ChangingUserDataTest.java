import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.After;

import io.qameta.allure.junit4.DisplayName;

import java.util.Map;

import static org.junit.Assert.assertEquals;

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
        Map<String, String> responseData = userOperation.registerUser();
        token = responseData.get("token");

        User existingUser = new UserBuilder()
                .setRandomEmail().build();
        ValidatableResponse responseExistingUser = userClient.changingUserData(responseData.get("token"), existingUser);
        responseExistingUser.assertThat().statusCode(200);

        boolean isUpdate = responseExistingUser.extract().path("success");
        assertEquals("User data is not update",true, isUpdate);
    }

    @DisplayName("Обновление имени у авторизованного пользователя")
    @Test
    public void changingNameAuthorizedUser() {
        Map<String, String> responseData = userOperation.registerUser();
        token = responseData.get("token");

        User existingUser = new UserBuilder().setRandomName().build();
        ValidatableResponse responseExistingUser = userClient.changingUserData(responseData.get("token"), existingUser);
        responseExistingUser.assertThat().statusCode(200);

        boolean isUpdate = responseExistingUser.extract().path("success");
        assertEquals("User data is not update",true, isUpdate);
    }

    @DisplayName("Обновление пароля у авторизованного пользователя")
    @Test
    public void changingPasswordAuthorizedUser() {
        Map<String, String> responseData = userOperation.registerUser();
        token = responseData.get("token");

        User existingUser = new UserBuilder().setRandomPassword().build();
        ValidatableResponse responseExistingUser = userClient.changingUserData(responseData.get("token"), existingUser);
        responseExistingUser.assertThat().statusCode(200);

        boolean isUpdate = responseExistingUser.extract().path("success");
        assertEquals("User data is not update",true, isUpdate);
    }

    @DisplayName("Обновление Email у пользователя без авторизации")
    @Test
    public void changingEmailNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomEmail().build();
        ValidatableResponse response = userClient.changingUserData(null, existingUser);
        response.assertThat().statusCode(401);

        boolean isUpdate = response.extract().path("success");
        assertEquals("User authorised",false, isUpdate);

        String errorMessage = response.extract().path("message");
        assertEquals("You should be authorised", errorMessage);
    }

    @DisplayName("Обновление имени у пользователя без авторизации")
    @Test
    public void changingNameNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomName().build();
        ValidatableResponse response = userClient.changingUserData(null, existingUser);
        response.assertThat().statusCode(401);

        boolean isUpdate = response.extract().path("success");
        assertEquals("User authorised",false, isUpdate);

        String errorMessage = response.extract().path("message");
        assertEquals("You should be authorised", errorMessage);
    }

    @DisplayName("Обновление пароля у пользователя без авторизации")
    @Test
    public void changingPasswordNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomName().build();
        ValidatableResponse response = userClient.changingUserData(null, existingUser);
        response.assertThat().statusCode(401);

        boolean isUpdate = response.extract().path("success");
        assertEquals("User authorised",false, isUpdate);

        String errorMessage = response.extract().path("message");
        assertEquals("You should be authorised", errorMessage);
    }
}
