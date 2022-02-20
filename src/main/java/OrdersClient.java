import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrdersClient extends RestAssuredClient{

    private final static String ORDERS_PATH = "/api/orders";

    @Step("create order")
    public ValidatableResponse createOrder(String token, Ingredients ingredients) {
        RequestSpecification request = given()
                .spec(getBaseSpec());

        if (token != null && ingredients != null){
            request.auth().oauth2(token.substring(7)).body(ingredients);
        } else if (token != null && ingredients == null) {
            request.auth().oauth2(token.substring(7));
        } else if (token == null && ingredients != null){
            request.body(ingredients);
        }

        return request
                .when()
                .post(ORDERS_PATH)
                .then()
                .log().all();
    }

    @Step("get all orders")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH +"/all")
                .then()
                .log().all();
    }

    @Step("get user order")
    public ValidatableResponse getUserOrder(String token) {
        RequestSpecification request = given()
                .spec(getBaseSpec());

        if (token != null) {
            request.auth().oauth2(token.substring(7));
        }
        return request
                .when()
                .get(ORDERS_PATH)
                .then()
                .log().all();
    }
}
