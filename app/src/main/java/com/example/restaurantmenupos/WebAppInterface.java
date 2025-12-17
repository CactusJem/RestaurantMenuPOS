package com.example.restaurantmenupos;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.restaurantmenupos.model.MenuItem;
import com.example.restaurantmenupos.model.Order;
import com.example.restaurantmenupos.model.OrderItem;
import com.example.restaurantmenupos.model.OrderRepository;

public class WebAppInterface {

    private final Context context;
    private final OrderRepository orderRepository;

    public WebAppInterface(Context context) {
        // Use applicationContext to avoid leaking the Activity
        this.context = context.getApplicationContext();
        this.orderRepository = OrderRepository.getInstance();
    }

    // Called from JS: AndroidApp.showToast("Hello")
    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Called from JS:
     * AndroidApp.addOrder(orderId, 0, customerName, total, 1)
     *
     * Matches the signature used in payment.js:
     *  - orderId: String
     *  - tableNumber: int
     *  - itemName: String  (we use customerName as a label)
     *  - price: double     (total order amount)
     *  - quantity: int
     */
    @JavascriptInterface
    public void addOrder(
            String orderId,
            int tableNumber,
            String itemName,
            double price,
            int quantity
    ) {
        // Build domain objects so you can show OOP usage
        MenuItem menuItem = new MenuItem("js-" + orderId, itemName, price);
        OrderItem orderItem = new OrderItem(menuItem, quantity);

        Order order = new Order(orderId,tableNumber);
        order.addItem(orderItem);

        orderRepository.addOrder(order);

        double total = order.calculateTotal();
        String toastMessage = "Android received order " + orderId + ", total: " + total;
        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
    }

    // Optional: can be called from JS to check how many orders native side has stored
    @JavascriptInterface
    public int getTotalOrders() {
        return orderRepository.getOrderCount();
    }
}
