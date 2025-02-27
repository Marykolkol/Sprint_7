import client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.LoginCredentials;
import model.LoginResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {
    private CourierClient courierClient = new CourierClient();
    private Courier courier;
    private LoginResponse loginResponse = new LoginResponse();

    @Before
    public void createNewCourier(){
        Map<String,String> data = courierClient.generateCourierBody();
        courier = new Courier(data.get("login"), data.get("password"), data.get("firstName"));
        courierClient.postCreateCourier(courier);
    }

    @Test
    @DisplayName("Логин курьера")
    public void loginSuccess(){
        LoginCredentials credentials = LoginCredentials.fromCourier(courier);
        ValidatableResponse response = courierClient.postLoginCourier(credentials);

        response.assertThat().statusCode(200);
        loginResponse = response.extract().body().as(LoginResponse.class);
        Assert.assertNotNull(loginResponse.getId());
    }

    @Test
    @DisplayName("Логин курьера без логина")
    public void loginCheckRequiredLogin(){
        courier.setLogin("");
        LoginCredentials credentials = LoginCredentials.fromCourier(courier);
        ValidatableResponse response = courierClient.postLoginCourier(credentials);

        response.assertThat().statusCode(400);
        response.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера без пароля")
    public void loginCheckRequiredPassword(){
        courier.setPassword("");
        LoginCredentials credentials = LoginCredentials.fromCourier(courier);
        ValidatableResponse response = courierClient.postLoginCourier(credentials);

        response.assertThat().statusCode(400);
        response.assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера с неправильным логином")
    public void loginCheckCorrectLogin(){
        courier.setLogin("haker");
        LoginCredentials credentials = LoginCredentials.fromCourier(courier);
        ValidatableResponse response = courierClient.postLoginCourier(credentials);

        response.assertThat().statusCode(404);
        response.assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера с неправильным паролем")
    public void loginCheckCorrectPassword(){
        courier.setPassword("haker");
        LoginCredentials credentials = LoginCredentials.fromCourier(courier);
        ValidatableResponse response = courierClient.postLoginCourier(credentials);

        response.assertThat().statusCode(404);
        response.assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @After
    public void deleteCourier() {
        if (courier != null) {
           courierClient.deleteCourier(loginResponse.getId());
        }
    }
}
