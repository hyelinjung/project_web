package shoppingMall.project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import shoppingMall.project.constant.OrderStatus;
import shoppingMall.project.domain.OrderDto;
import shoppingMall.project.domain.OrderHistoDto;
import shoppingMall.project.entity.*;
import shoppingMall.project.repository.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ImgRepository imgRepository;
    @Autowired
    OrderItemRepository orderItemRepository;



        public void getOrder(OrderDto orderDto,String username) throws Exception {
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            List<OrderItem> orderItems = new ArrayList<>();
            //OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            //orderItemRepository.save(orderItem);
            //orderItems.add(orderItem);
            User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
            Order order = new Order(user, OrderStatus.SOLD);
            OrderItem orderItem1 = OrderItem.createOrderItem(item,orderDto.getCount(),order);
            orderItems.add(orderItem1);
            order.setOrderItems(orderItems);
            order.addTotalPrice(orderItems);
            orderRepository.save(order);
            orderItemRepository.save(orderItem1);
            //Order order = Order.createOrder(user,orderItems);
            //orderRepository.save(order);
        }

       /* public void getCartOrder(List<OrderDto> orderDtos,String username) throws Exception {
            User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
            List<OrderItem> orderItemList = new ArrayList<>();
            for (OrderDto orderDto :orderDtos){
                Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
                OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
                orderItemRepository.save(orderItem);
                orderItemList.add(orderItem);
            }
            Order order = Order.createOrder(user,orderItemList);
            orderRepository.save(order);
        }*/

    //주문한 상품 리스트 페이지 -> 대표상품 1개만
        @Transactional(readOnly = true)
        public Page<OrderHistoDto> getOrderList(String username,Pageable pageable){
            System.out.println("내역 서비스");
            Long count = orderRepository.findOrderCount(username);
            List<Order> orders = orderRepository.findOrder(username,pageable);


            System.out.println("결과:"+ Arrays.toString(orders.toArray()));
            System.out.println("====");

        List<OrderHistoDto> orderHistoDtos = new ArrayList<>();
            for (Order order : orders){
                System.out.println("테스트");
                List<OrderItem> orderItems = order.getOrderItems();
                OrderItem orderItem = orderItems.get(0); //대표 상품만
                Long itemId = orderItem.getItem().getId();
                int price = orderItem.getItem().getDiscountedPrice()*orderItem.getCount(); //주문한 상품의 총합
                ItemImg itemImg =imgRepository.findByItemIdAndImgYn(itemId,"Y");
                OrderHistoDto histoDto = new OrderHistoDto(order,itemImg.getImgUrl(),orderItem.getItem().getItemNm(),orderItem.getCount(),price,orderItem.getId(),orderItem.getItem().getId());
                orderHistoDtos.add(histoDto);
            }
            System.out.println("들어옴"+Arrays.toString(orderHistoDtos.toArray()));
        return new PageImpl<>(orderHistoDtos,pageable,count);
        }

        public boolean checkVail(String username,Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        String savedUser = order.getUser().getUsername();
        if (!StringUtils.equals(username,savedUser)){
            return false;
        }
        return true;
        }

        public void cancelOrder(Long orderId){
            System.out.println("들어온값:"+orderId);
            System.out.println("주문 취소 서비스");
            Order order = orderRepository.findOrder(orderId).orElseThrow(EntityNotFoundException::new);
            System.out.println(order);
            order.orderCancel();
            System.out.println("상태"+order.getOrderStatus());
           orderRepository.save(order);
        }
        //해당 주문의 상품 리스트들을 얻어옴
        @Transactional(readOnly = true)
        public List<OrderHistoDto> orderDetl(Long id ){
            System.out.println("주문 상품 디테일 ");
            Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            //List<OrderItemDto> orderItemDtos = new ArrayList<>();
            List<OrderHistoDto> orderItemDtos = new ArrayList<>();
           /* for (OrderItem orderItem : order.getOrderItems()){
                Item item = orderItem.getItem();
                String yn = "Y";
                ItemImg itemImg = imgRepository.findByItemIdAndImgYn(item.getId(),yn);
                OrderItemDto dto = new OrderItemDto(orderItem,itemImg.getImgUrl());
                orderItemDtos.add(dto);
            }*/
            for (OrderItem orderItem : order.getOrderItems()){
                Item item = orderItem.getItem();
                ItemImg itemImg = imgRepository.findByItemIdAndImgYn(item.getId(),"Y");
                int price = item.getDiscountedPrice()*orderItem.getCount();
                int discountedPrice = item.getPrice()*orderItem.getCount();
                OrderHistoDto histoDto = new OrderHistoDto(order,itemImg.getImgUrl(),item.getItemNm(),orderItem.getCount(),price,orderItem.getId(),item.getId());
                orderItemDtos.add(histoDto);
            }
            System.out.println("주문상세 :" + Arrays.toString(orderItemDtos.toArray()));
           return orderItemDtos;
        }

        //해당 주문의 전체 상품의 값의 합 -> 주문상품 내역 list dto에 필드값으로 하기엔 필요없는 반복으로 낭비가 많아 따로 뺐다
        public int totalOrderItemListPrice(Long id){
          /* return orderRepository.findById(id)
                    .map(entity -> entity.getOrderTotalPrice())
                    .orElseThrow(IllegalAccessError::new);*/
            Order order = orderRepository.findById(id).orElseThrow(IllegalAccessError::new);
            return order.getOrderTotalPrice();
        }

        //리뷰쓰기에서 해당 상품을 구매한 사용자인지 판단하기위한
    public boolean checkReviewer(String username,Long id){
        System.out.println("리뷰 가능 채크");
        System.out.println(id);
            User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
            List<Order> orders = orderRepository.findReviewer(username);
            if (orders.isEmpty()){
                System.out.println("구매 이력 업슴");
                return false;
            }
            for (Order order :orders){
                System.out.println("구매함");
               for (OrderItem orderItem : order.getOrderItems()){
                   if (orderItem.getItem().getId() == id){
                       System.out.println("같음");
                       return true;
                   }
               }
            }
            return false;
    }

}
