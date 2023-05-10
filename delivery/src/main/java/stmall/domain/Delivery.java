package stmall.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import stmall.DeliveryApplication;
import stmall.domain.DeliveryCancelled;
import stmall.domain.DeliveryStarted;

@Entity
@Table(name = "Delivery_table")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String productName;
    private Long productId;
    private Long userId;
    private Integer qty;
    private String status;
    private String courier;

    @PostPersist
    public void onPostPersist() {
        // DeliveryStarted deliveryStarted = new DeliveryStarted(this);
        // deliveryStarted.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        // DeliveryCancelled deliveryCancelled = new DeliveryCancelled(this);
        // deliveryCancelled.publishAfterCommit();
    }

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }

    public void deliveryComplete(DeliveryCompleteCommand deliveryCompleteCommand) {
        this.setCourier(deliveryCompleteCommand.getCourier());
        this.setStatus("DeliveryCompleted");

        DeliveryCompleted deliveryCompleted = new DeliveryCompleted(this);
        deliveryCompleted.publishAfterCommit();
    }

    public void deliveryReturn(DeliveryReturnCommand deliveryReturnCommand) {
        this.setCourier(deliveryReturnCommand.getCourier());
        this.setStatus("DeliveryReturned");

        DeliveryReturned deliveryReturned = new DeliveryReturned(this);
        deliveryReturned.publishAfterCommit();
    }

    public static void startDelivery(OrderPlaced orderPlaced) {
        /** Example 1:  new item         */
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderPlaced.getId());
        delivery.setProductId(orderPlaced.getProductId());
        delivery.setProductName(orderPlaced.getProductName());
        delivery.setQty(orderPlaced.getQty());
        delivery.setUserId(orderPlaced.getUserId());
        delivery.setStatus("DeliveryStarted");
        repository().save(delivery);

        DeliveryStarted deliveryStarted = new DeliveryStarted(delivery);
        deliveryStarted.publishAfterCommit();


    }

    public static void cancelDelivery(OrderCancelled orderCancelled) {
        /** Example 2:  finding and process     */
        
        repository().findByOrderId(orderCancelled.getId()).ifPresent(delivery->{
            delivery.setStatus("DeliveryCancelled");
            repository().save(delivery);

            DeliveryCancelled deliveryCancelled = new DeliveryCancelled(delivery);
            deliveryCancelled.publishAfterCommit();

         });
   

    }
}
