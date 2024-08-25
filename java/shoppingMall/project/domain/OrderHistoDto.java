package shoppingMall.project.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import shoppingMall.project.constant.OrderStatus;
import shoppingMall.project.entity.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//주문완료한 상품리스트 페이지 DTO
@Data
@NoArgsConstructor
public class OrderHistoDto {


    private Long orderId;
    private Long orderItemId;
    private OrderStatus orderStatus;
    //private int totalOrderPrice; //주문한 상품들의 총합
    private LocalDateTime localDateTime; //주문완료한 날짜
    private String imgUrl;
    private String itemNm;
    private int orderedCount;
    private int itemTotalPrice; //주문한 상품 한개 의 총합
    private Long itemId;
    //private List<OrderItemDto> orderItemDtos = new ArrayList<>();

   /* public void addOrderItem(List<OrderItemDto> orderItemDtoList){
        for (OrderItemDto dto : orderItemDtoList){
            orderItemDtos.add(dto);
        }
    }*/

    /*public OrderHistoDto(List<OrderItemDto> orderItemDtoList, Order order) {
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.totalOrderPrice = order.getOrderTotalPrice();
        this.localDateTime = order.getOrderDateTime();
        addOrderItem(orderItemDtoList);
    }*/
    public OrderHistoDto( Order order,String imgUrl,String itemNm,int orderedCount,int itemTotalPrice,Long orderItemId,Long itemId) {
        this.orderId = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.itemTotalPrice = itemTotalPrice;
        this.localDateTime = order.getOrderDateTime();
        this.imgUrl=imgUrl;
        this.itemNm = itemNm;
        this.orderedCount = orderedCount;
        this.orderItemId =orderItemId;
        this.itemId = itemId;
    }
}
