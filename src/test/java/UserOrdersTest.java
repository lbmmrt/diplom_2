import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void getOrdersUserWithAuthorization() {
        Map<String, String> responseData = userOperation.registerUser();
        ValidatableResponse response = ordersClient.getUserOrder(responseData.get("token"));
        response.assertThat().statusCode(200);

        boolean isGetUserOrders = response.extract().path("success");
        Assert.assertTrue("User orders not received", isGetUserOrders);

        token = responseData.get("token");
    }

    @Test
    public void getOrdersUserWithoutAuthorization() {
        ValidatableResponse response = ordersClient.getUserOrder(null);
        response.assertThat().statusCode(401);

        boolean isGetUserOrders = response.extract().path("success");
        Assert.assertFalse("User orders received", isGetUserOrders);

        String errorMessage = response.extract().path("message");
        assertEquals("You should be authorised", errorMessage);
    }
}
