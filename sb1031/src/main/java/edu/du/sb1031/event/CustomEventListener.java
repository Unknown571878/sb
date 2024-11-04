package edu.du.sb1031.event;

import edu.du.sb1031.order.Order;
import edu.du.sb1031.shipment.Shipment;
import edu.du.sb1031.shipment.ShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@Slf4j
public class CustomEventListener {

    @Autowired
    ShipmentService shipmentService;

//    @EventListener
//    public void handleCustomEvent(CustomEvent customEvent) {
//        log.info("Handling context started event. message : {}", customEvent.getMessage());
//    }
    @EventListener
    public void createShipment(CustomEvent customEvent) {
        Order order = customEvent.getOrder();
        if (order == null) {
            log.error("Received a custom event with a null order.");
            return; // null인 경우 처리
        }

        log.info("Creating shipment for order: {}", order);
//        Shipment shipment = new Shipment();
//        shipment.setPrice(order.getPrice());
//        shipment.setQuantity(order.getQuantity());
//        shipment.setProductName(order.getProductName());

        Shipment shipment = Shipment.builder()
                .price(order.getPrice())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .build();
        shipmentService.saveShipment(shipment);
    }
}
