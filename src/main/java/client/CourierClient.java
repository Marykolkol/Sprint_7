package client;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Courier;
import model.LoginCredentials;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static config.ScooterConfig.SCOOTER_BASE_URI;
import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final Random RANDOM = new Random();

    public Map<String, String> generateCourierBody(){
        Map<String, String> body = new HashMap<>();
        body.put("login", "login_" + RANDOM.nextInt(1000));
        body.put("password", "password_" + RANDOM.nextInt(1000));
        body.put("firstName", "Test Testov");
        return body;
    }

    @Step
    @DisplayName("Создание курьера")
    public ValidatableResponse postCreateCourier(Courier courier){
        return
            given()
                    .log()
                    .all()
                .baseUri(SCOOTER_BASE_URI)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                    .log()
                    .all();
    }

    @Step
    @DisplayName("Логин курьера")
    public ValidatableResponse postLoginCourier(LoginCredentials credentials){
        return
                given()
                        .log()
                        .all()
                        .baseUri(SCOOTER_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(credentials)
                        .when()
                        .post("/api/v1/courier/login")
                        .then()
                        .log()
                        .all();
    }

    @Step
    @DisplayName("Удаление курьера")
    public ValidatableResponse deleteCourier(String id){
        return
                given()
                        .log()
                        .all()
                        .baseUri(SCOOTER_BASE_URI)
                        .header("Content-type", "application/json")
                        .when()
                        .delete("/api/v1/courier/:" + id)
                        .then();
    }
}
