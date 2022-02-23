import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import io.qameta.allure.junit4.DisplayName;
import static org.junit.Assert.assertEquals;

import java.util.Map;


public class UserOrdersTest {

    private UserClient userClient;
    private String token;

    UserOperation userOperation = new UserOperation();
    OrdersClient ordersClient = new OrdersClient();

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

    @DisplayName("Создание заказа через авторизованного пользователя")
    @Test
    public void getOrdersUserWithAuthorization() {
        Map<String, String> responseData = userOperation.registerUserAndGetData();
        token = responseData.get("token");

        ValidatableResponse response = ordersClient.getUserOrder(responseData.get("token"));
        response.assertThat().statusCode(200);

        boolean actual = response.extract().path("success");
        assertEquals("User orders not received", true, actual);
    }

    @DisplayName("Создание заказа через пользователя без авторизации")
    @Test
    public void getOrdersUserWithoutAuthorization() {
        ValidatableResponse response = ordersClient.getUserOrder(null);
        response.assertThat().statusCode(401);

        boolean isGetUserOrders = response.extract().path("success");
        assertEquals("User orders received", false, isGetUserOrders);

        String errorMessage = response.extract().path("message");
        assertEquals("You should be authorised", errorMessage);
    }
}
