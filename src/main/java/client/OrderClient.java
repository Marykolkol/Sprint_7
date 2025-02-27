package client;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static config.ScooterConfig.SCOOTER_BASE_URI;
import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final Random RANDOM = new Random();

    public Map<String, Object> generateOrderBody(){
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Test");
        body.put("lastName", "Testov");
        body.put("address", "Test Adress");
        body.put("metroStation", "Nanaevskaya");
        body.put("phone", "+79999999" + RANDOM.nextInt(1000));
        body.put("rentTime", RANDOM.nextInt(10));
        body.put("deliveryDate", LocalDateTime.now().toString());
        body.put("comment", "Test comment");
        return body;
    }

    @Step
    @DisplayName("Создание заказа")
    public ValidatableResponse postCreateOrder(Order order){
        return
                given()
                        .log()
                        .all()
                        .baseUri(SCOOTER_BASE_URI)
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post("/api/v1/orders")
                        .then()
                        .log()
                        .all();
    }

    @Step
    @DisplayName("Отмена заказа")
    public ValidatableResponse putCancelOrder(String track){
        return
                given()
                        .log()
                        .all()
                        .baseUri(SCOOTER_BASE_URI)
                        .header("Content-type", "application/json")
                        .queryParam("track", track)
                        .when()
                        .put("/api/v1/orders/cancel")
                        .then();
    }

    @Step
    @DisplayName("Получение списка заказов")
    public ValidatableResponse getListOfOrders(){
        return
                given()
                        .log()
                        .all()
                        .baseUri(SCOOTER_BASE_URI)
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders")
                        .then()
                        .log()
                        .all();
    }
}
