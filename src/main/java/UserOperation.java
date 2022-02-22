import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

public class UserOperation {

    static final String RESPONSE_SUCCESS_TRUE = "1";
    static final String RESPONSE_SUCCESS_FALSE = "0";

    UserClient userClient = new UserClient();


    @Step("Регистрация нового пользователя")
    public Map<String, String> registerUser() {
        User user = new UserBuilder()
                .setRandomParams()
                .build();
        ValidatableResponse response = userClient.registerUser(user);

        Map<String, String> responseData = new HashMap<>();
        if (response != null) {
            responseData.put("email", user.getEmail());
            responseData.put("name", user.getName());
            responseData.put("password", user.getPassword());
            responseData.put("token", response.extract().path("accessToken"));
            responseData.put("success", response.extract().path("success") ? RESPONSE_SUCCESS_TRUE : RESPONSE_SUCCESS_FALSE);
            responseData.put("statusCode", String.valueOf(response.extract().statusCode()));
        }

        return responseData;
    }

//    @Step("Обновление информации о пользователе")
//    public void updateUserData(String token, User existingUser) {
//        ValidatableResponse responseExistingUser = userClient.changingUserData(token, existingUser);
//        responseExistingUser.assertThat().statusCode(200);
//        boolean isUpdate = responseExistingUser.extract().path("success");
//        assertEquals("User data is not update",true, isUpdate);
//    }
//
//    public void updateDataFail(User existingUser) {
//        ValidatableResponse response = userClient.changingUserData(null, existingUser);
//        response.assertThat().statusCode(401);
//        boolean isUpdate = response.extract().path("success");
//        assertEquals("User authorised",false, isUpdate);
//
//        String errorMessage = response.extract().path("message");
//        assertEquals("You should be authorised", errorMessage);
//    }
}
