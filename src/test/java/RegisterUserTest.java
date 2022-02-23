import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class RegisterUserTest {

    private UserClient userClient;
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
        User user = new UserBuilder()
                .setRandomParams()
                .build();

        ValidatableResponse response = userClient.registerUser(user);
        token = response.extract().path("accessToken");
        response.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Test
    public void registeringAnExistingUser() {
        User user = new UserBuilder()
                .setRandomParams()
                .build();
        ValidatableResponse response = userClient.registerUser(user);
        token = response.extract().path("accessToken");

        User existingUser = new UserBuilder()
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setName(user.getName())
                .build();

        ValidatableResponse responseExistingUser = userClient.registerUser(existingUser);
        responseExistingUser.assertThat().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @DisplayName("Создание пользователя без заполнения имени")
    @Test
    public void registeringWithoutName() {
        User newUser = new UserBuilder()
                .setRandomEmail()
                .setRandomPassword()
                .build();

        ValidatableResponse response = userClient.registerUser(newUser);
        response.assertThat().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}
