import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private final static String USER_PATH = "/api";

    @Step("Регистрация пользователя")
    public ValidatableResponse registerUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/auth/register")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .when()
                .delete(USER_PATH +"/auth/user")
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH +"/auth/login")
                .then();
    }

    @Step("Обновление информации о пользователе")
    public ValidatableResponse changingUserData(String token, User user) {
        RequestSpecification request = given()
                .spec(getBaseSpec());

        if (token != null) {
            request.auth().oauth2(token.substring(7));
        }
        return request
                .body(user)
                .when()
                .patch(USER_PATH +"/auth/user")
                .then();
    }

    @Step("Сброс пароля")
    public ValidatableResponse passwordReset(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/api/password-reset")
                .then();
    }

    @Step("Обновление токена")
    public ValidatableResponse updateToken(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/auth/token")
                .then();
    }
}
