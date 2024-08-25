package shoppingMall.project.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@ToString(exclude = "order") //오버플로우발생 무한참조예방
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orderItem_id")
    private Long id;
    private int count;
    //private int orderPrice; // 상품 화면에서 수량에따라 총 가격, != 장바구니에서 상품들의 총 가격이 아님
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDateTime regDateTime;


   /* public static OrderItem createOrderItem(Item item, int count) throws Exception {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderPrice(item.getPrice()*count); // count * item.getPrice() ..?
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.reduceStock(count);
        orderItem.setRegDateTime(LocalDateTime.now());
        return orderItem;
    }*/
   public static OrderItem createOrderItem(Item item, int count,Order order) throws Exception {
       OrderItem orderItem = new OrderItem();
       orderItem.setItem(item);
       orderItem.setCount(count);
       item.minStock(count);
       orderItem.setRegDateTime(LocalDateTime.now());
       orderItem.setOrder(order);
       return orderItem;
   }







   /* public void reduceStock(int count) throws Exception { //주문시 수량 감소
        this.item.minStock(count);
    }*/
    public void addStock(){
        this.item.addStock(count);
    }
}
