import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends RestAssuredClient {

    private final static String COURIER_PATH = "/api/ingredients";

    @Step("get all ingredients")
    public ValidatableResponse getAllIngredients() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(COURIER_PATH)
                .then()
                .log().all();
    }
}
