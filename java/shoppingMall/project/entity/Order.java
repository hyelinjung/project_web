package shoppingMall.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import shoppingMall.project.constant.OrderStatus;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*1. 나의 주문내역에서 모든 주문내역을 알고 싶다 -> 양방향 필요*/
@Entity
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    private LocalDateTime orderDateTime;
    private LocalDateTime orderCancelTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //sold, cancel

    private int orderTotalPrice ; //주문한 상품의 총 합

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


/*    public void changeOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }*/
   /* public static Order createOrder(User user,List<OrderItem> orderItemList){
        Order order = new Order();
        int op = 0; //orderTotalPrice
        order.setOrderStatus(OrderStatus.SOLD);
        order.setOrderDateTime(LocalDateTime.now());
        order.setUser(user);
        for (OrderItem orderItem : orderItemList){
           order.changeOrderItem(orderItem);
           op += orderItem.getOrderPrice();
        }
        order.setOrderTotalPrice(op);
        return order;
    }*/

    public Order(User user, OrderStatus orderStatus) {
        this.user = user;
        orderDateTime = LocalDateTime.now();
        orderCancelTime = LocalDateTime.now();
        this.orderStatus = orderStatus;
    }
    public void addTotalPrice(List<OrderItem> orderItems){ // 주문한 상품들의 총 합 -> 세일 값이 없을 때도 일반가격이니깐 세일 값으로 구하기
        for (OrderItem orderItem: orderItems){
            orderTotalPrice+=(orderItem.getCount()*orderItem.getItem().getDiscountedPrice());
        }

    }

    //주문 취소 시 재고다시 +
    public void orderCancel(){
        System.out.println("주문취소 엔티티");
        this.orderStatus= OrderStatus.CANCEL;
        for (OrderItem orderItem :orderItems){
            orderItem.addStock();
        }


    }
}
