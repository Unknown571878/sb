package edu.du.sb1031.event;

import edu.du.sb1031.order.Order;
import org.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {

//    private String message;

    private Order order;

//    public CustomEvent(Object source, String message) {
//        super(source);
//        this.message = message;
//    }

    public CustomEvent(Object source, Order order) {
        super(source);
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.order = order;
    }

//    public String getMessage() {return message;}

    public Order getOrder() {return order;}
}
