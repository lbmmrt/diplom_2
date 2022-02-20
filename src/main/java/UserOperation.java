import io.restassured.response.ValidatableResponse;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserOperation {

    UserClient userClient = new UserClient();

    public Map<String, String> registerUser() {
        User user = new UserBuilder()
                .setRandomParams()
                .build();
        ValidatableResponse response = userClient.registerUser(user);
        response.assertThat().statusCode(200);
        boolean isRegistered = response.extract().path("success");
        Assert.assertTrue("User is not registered", isRegistered);

        Map<String, String> responseData = new HashMap<>();
        if (response != null) {
            responseData.put("email", user.getEmail());
            responseData.put("name", user.getName());
            responseData.put("password", user.getPassword());
            responseData.put("token", response.extract().path("accessToken"));
        }
        return responseData;
    }

    public void updateDataSuccess(String token, User existingUser) {
        ValidatableResponse responseExistingUser = userClient.changingUserData(token, existingUser);
        responseExistingUser.assertThat().statusCode(200);
        boolean isUpdate = responseExistingUser.extract().path("success");
        Assert.assertTrue("User data is not update", isUpdate);
    }

    public void updateDataFail(User existingUser) {
        ValidatableResponse response = userClient.changingUserData(null, existingUser);
        response.assertThat().statusCode(401);
        boolean isUpdate = response.extract().path("success");
        Assert.assertFalse("User authorised", isUpdate);

        String errorMessage = response.extract().path("message");
        assertEquals("You should be authorised", errorMessage);
    }
}
