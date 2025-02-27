import client.OrderClient;
import io.qameta.allure.junit4.DisplayName;
import model.Order;
import model.OrderResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ListOfOrdersTest {
    private OrderClient orderClient = new OrderClient();
    private OrderResponse orderResponse = new OrderResponse();
    Order order;

    @Before
    public void createOrder(){
        Map<String, Object> data = orderClient.generateOrderBody();
        order = new Order(data.get("firstName").toString(), data.get("lastName").toString(), data.get("address").toString(),
                data.get("metroStation").toString(), data.get("phone").toString(), (int) data.get("rentTime"),
                data.get("deliveryDate").toString(), data.get("comment").toString());
        orderResponse = orderClient.postCreateOrder(order).extract().body().as(OrderResponse.class);
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getListOfOrders(){
        List<Order> listOfOrders =
                orderClient.getListOfOrders().extract().jsonPath().getList("orders", Order.class);

        Assert.assertNotNull(listOfOrders);
    }

    @After
    public void deleteOrder(){
        if (order != null) {
            orderClient.putCancelOrder(orderResponse.getTrack());
        }
    }
}
