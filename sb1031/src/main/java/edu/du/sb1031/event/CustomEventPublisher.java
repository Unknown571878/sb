package edu.du.sb1031.event;

import edu.du.sb1031.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomEventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

//    public void doStuffAndPublishAnEvent(final String message) {
//        System.out.println("Publishing custom event. ");
//        CustomEvent customSpringEvent = new CustomEvent(this, message);
//        applicationEventPublisher.publishEvent(customSpringEvent);
//    }

    public void createShipment(final Order order) {
        CustomEvent customSpringEvent = new CustomEvent(this, order);
        applicationEventPublisher.publishEvent(customSpringEvent);
    }
}
