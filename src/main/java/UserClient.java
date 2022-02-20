import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {

    private final static String USER_PATH = "/api";

    @Step("register user")
    public ValidatableResponse registerUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/auth/register")
                .then()
                .log().all();
    }

    @Step("delete user")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(token.substring(7))
                .when()
                .delete(USER_PATH +"/auth/user")
                .then()
                .log().all();
    }

    @Step("login user")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH +"/auth/login")
                .then()
                .log().all();
    }

    @Step("changing user data")
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
                .then()
                .log().all();
    }

    @Step("password reset")
    public ValidatableResponse passwordReset(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/api/password-reset")
                .then()
                .log().all();
    }

    @Step("update token")
    public ValidatableResponse updateToken(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "/auth/token")
                .then()
                .log().all();
    }
}
