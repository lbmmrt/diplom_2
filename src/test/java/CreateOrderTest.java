import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void createOrderWithoutAuthorization() {
        Ingredients ingredients = new IngredientsBuilder().setRandomIngredient().build();
        ValidatableResponse response = ordersClient.createOrder(null, ingredients);
        response.assertThat().statusCode(200);
        boolean isOk = response.extract().path("success");
        Assert.assertTrue("Order is not created", isOk);
    }

    @Test
    public void createOrderWithAuthorization() {
        Ingredients ingredients = new IngredientsBuilder().setRandomIngredient().build();
        Map<String, String> responseData = userOperation.registerUser();
        ValidatableResponse response = ordersClient.createOrder(responseData.get("token"), ingredients);
        response.assertThat().statusCode(200);
        boolean isOk = response.extract().path("success");
        Assert.assertTrue("Order is not created", isOk);

        token = responseData.get("token");
    }

    @Test
    public void createOrderWithoutIngredients() {
        ValidatableResponse response = ordersClient.createOrder(null, null);
        response.assertThat().statusCode(400);
        boolean isOk = response.extract().path("success");
        Assert.assertFalse("Order is created", isOk);

        String errorMessage = response.extract().path("message");
        assertEquals("Ingredient ids must be provided", errorMessage);
    }

    @Test
    public void createOrderWithoutInvalidIngredients() {
        Ingredients invalidIngredients = new IngredientsBuilder().setRandomInvalidIngredient().build();
        ValidatableResponse response = ordersClient.createOrder(null, invalidIngredients);
        response.assertThat().statusCode(500);
    }
}
