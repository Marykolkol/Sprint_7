import client.CourierClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.LoginCredentials;
import model.LoginResponse;
import org.junit.After;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class CreateCourierTest {

    private CourierClient courierClient = new CourierClient();
    private Courier courier;
    private LoginResponse loginResponse = new LoginResponse();

    @Test
    @DisplayName("Создание курьера")
    public void createCourier(){
        Map<String,String> data = courierClient.generateCourierBody();
        courier = new Courier(data.get("login"), data.get("password"), data.get("firstName"));
        ValidatableResponse response = courierClient.postCreateCourier(courier);

        response.assertThat().statusCode(201);
        response.assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание курьера с имеющимся логином")
    public void createDuplicateCourier(){
        Map<String, String> data = courierClient.generateCourierBody();
        courier = new Courier(data.get("login"), data.get("password"), data.get("firstName"));
        courierClient.postCreateCourier(courier);

        ValidatableResponse responseDuplicate = courierClient.postCreateCourier(courier);
        responseDuplicate.assertThat().statusCode(409);
        responseDuplicate.assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    public void createCourierCheckRequiredLogin(){
        Map<String,String> data = courierClient.generateCourierBody();
        courier = new Courier("", data.get("password"), data.get("firstName"));
        ValidatableResponse response = courierClient.postCreateCourier(courier);

        response.assertThat().statusCode(400);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierCheckRequiredPassword(){
        Map<String,String> data = courierClient.generateCourierBody();
        courier = new Courier(data.get("login"), "", data.get("firstName"));
        ValidatableResponse response = courierClient.postCreateCourier(courier);

        response.assertThat().statusCode(400);
        response.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteCourier() {
        if (courier != null) {
            LoginCredentials credentials = LoginCredentials.fromCourier(courier);
            loginResponse = courierClient.postLoginCourier(credentials).extract().body().as(LoginResponse.class);
            courierClient.deleteCourier(loginResponse.getId());
        }
    }
}
