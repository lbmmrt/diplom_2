import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

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
        Map<String, String> responseData = userOperation.registerUser();
        User existingUser = new UserBuilder()
                .setRandomEmail().build();
        userOperation.updateDataSuccess(responseData.get("token"), existingUser);
    }

    @DisplayName("Обновление имени у авторизованного пользователя")
    @Test
    public void changingNameAuthorizedUser() {
        Map<String, String> responseData = userOperation.registerUser();
        User existingUser = new UserBuilder().setRandomName().build();
        userOperation.updateDataSuccess(responseData.get("token"),existingUser);
    }

    @DisplayName("Обновление пароля у авторизованного пользователя")
    @Test
    public void changingPasswordAuthorizedUser() {
        Map<String, String> responseData = userOperation.registerUser();
        User existingUser = new UserBuilder().setRandomPassword().build();
        userOperation.updateDataSuccess(responseData.get("token"),existingUser);
    }

    @DisplayName("Обновление Email у пользователя без авторизации")
    @Test
    public void changingEmailNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomEmail().build();
        userOperation.updateDataFail(existingUser);
    }

    @DisplayName("Обновление имени у пользователя без авторизации")
    @Test
    public void changingNameNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomName().build();
        userOperation.updateDataFail(existingUser);
    }

    @DisplayName("Обновление пароля у пользователя без авторизации")
    @Test
    public void changingPasswordNotAuthorizedUser() {
        User existingUser = new UserBuilder().setRandomName().build();
        userOperation.updateDataFail(existingUser);
    }
}
