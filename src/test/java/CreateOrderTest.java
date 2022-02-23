import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {

    OrdersClient ordersClient = new OrdersClient();
    UserOperation userOperation = new UserOperation();
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

    @DisplayName("Создание заказа с ингредиентами без авторизации")
    @Test
    public void createOrderWithoutAuthorization() {
        Ingredients ingredients = new IngredientsBuilder().setRandomIngredient().build();
        ValidatableResponse response = ordersClient.createOrder(null, ingredients);
        response.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @DisplayName("Создание заказа с ингредиентами и авторизацией")
    @Test
    public void createOrderWithAuthorization() {
        Ingredients ingredients = new IngredientsBuilder().setRandomIngredient().build();
        Map<String, String> responseData = userOperation.registerUserAndGetData();
        token = responseData.get("token");

        ValidatableResponse response = ordersClient.createOrder(responseData.get("token"), ingredients);
        response.assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @DisplayName("Создание заказа без ингредиентов и авторизации")
    @Test
    public void createOrderWithoutIngredients() {
        ValidatableResponse response = ordersClient.createOrder(null, null);
        response.assertThat().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @DisplayName("Создание заказа с невалидным ингредиентом без авторизации")
    @Test
    public void createOrderWithoutInvalidIngredients() {
        Ingredients invalidIngredients = new IngredientsBuilder().setRandomInvalidIngredient().build();
        ValidatableResponse response = ordersClient.createOrder(null, invalidIngredients);
        response.assertThat().statusCode(500);
    }
}
