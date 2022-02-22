import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import io.qameta.allure.junit4.DisplayName;

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

    @DisplayName("Создание уникального пользователя")
    @Test
    public void registerUser() {
        Map<String, String> responseData = userOperation.registerUser();
        token = responseData.get("token");
        String statusCode = responseData.get("statusCode");
        String isRegistered = responseData.get("success");
        assertEquals(statusCode, "200");
        assertEquals("User is not registered", UserOperation.RESPONSE_SUCCESS_TRUE, isRegistered);
    }

    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Test
    public void registeringAnExistingUser() {
        Map<String, String> responseData = userOperation.registerUser();
        token = responseData.get("token");

        User existingUser = new UserBuilder()
                .setEmail(responseData.get("email"))
                .setPassword(responseData.get("password"))
                .setName(responseData.get("name"))
                .build();
        ValidatableResponse responseExistingUser = userClient.registerUser(existingUser);
        responseExistingUser.assertThat().statusCode(403);
        boolean isRegisteredExistingUser = responseExistingUser.extract().path("success");
        assertEquals("User is registered", false, isRegisteredExistingUser);
        String errorMessage = responseExistingUser.extract().path("message");
        assertEquals("User already exists", errorMessage);
    }

    @DisplayName("Создание пользователя без заполнения имени")
    @Test
    public void registeringWithoutName() {
        User newUser = new UserBuilder()
                .setRandomEmail()
                .setRandomPassword()
                .build();
        ValidatableResponse response = userClient.registerUser(newUser);
        response.assertThat().statusCode(403);

        boolean isRegistered = response.extract().path("success");
        assertEquals("User is registered", false, isRegistered);

        String errorMessage = response.extract().path("message");
        assertEquals("Email, password and name are required fields", errorMessage);
    }
}
