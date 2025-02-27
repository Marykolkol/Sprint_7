import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.Order;
import model.OrderResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Map;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private OrderClient orderClient = new OrderClient();
    private OrderResponse orderResponse = new OrderResponse();
    private Order order;

    private String[] color;

    public CreateOrderTest(String[] color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] testData() {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK","GREY"}},
                {new String[]{}}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrder()
    {
        Map<String, Object> data = orderClient.generateOrderBody();
        order = new Order(data.get("firstName").toString(), data.get("lastName").toString(), data.get("address").toString(),
                data.get("metroStation").toString(), data.get("phone").toString(), (int) data.get("rentTime"),
                data.get("deliveryDate").toString(), data.get("comment").toString());
        order.setColor(color);
        ValidatableResponse response = orderClient.postCreateOrder(order);

        response.assertThat().statusCode(201);
        orderResponse = response.extract().body().as(OrderResponse.class);
        Assert.assertNotNull(orderResponse.getTrack());
    }

    @After
    public void deleteOrder(){
        if (order != null) {
            orderClient.putCancelOrder(orderResponse.getTrack());
        }
    }
}
