package shoppingMall.project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import shoppingMall.project.constant.OrderStatus;
import shoppingMall.project.domain.CartDetailDto;
import shoppingMall.project.domain.CartOrderDto;
import shoppingMall.project.domain.CartOrderOr;
import shoppingMall.project.domain.OrderDto;
import shoppingMall.project.entity.*;
import shoppingMall.project.repository.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    //상품 장바구니에 추가
    public void addCartItem(CartOrderDto dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Cart cart =  cartRepository.findByUserId(user.getId());
        if (cart == null) {
            cartRepository.save(Cart.createCart(user));
            cart = cartRepository.findByUserId(user.getId());
        }
        Item item = itemRepository.findById(dto.getItemId()).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        if (cartItem == null) {
            cartItem = CartItem.createCartItem(item, dto.getCount(), cart);
            cartItemRepository.save(cartItem);
            return;
        }
        cartItem.addCount(dto.getCount());
        cartItemRepository.save(cartItem);

    }



    //장바구니 상품 리스트
    public List<CartDetailDto> cartItemList(String username) {
        List<CartDetailDto> cartDetailDtos = new ArrayList<>();
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByUserId(user.getId());
        if (cart == null) { //상품을 한 번도 주문한 적없으면 빈화면 출력
            return cartDetailDtos;
        }
        cartDetailDtos = cartItemRepository.findCartDetailDtoList(cart.getId());
        System.out.println("쇼핑몰 상품들:"+ Arrays.toString(cartDetailDtos.toArray()));
        return cartDetailDtos;
      }

    //장바구니에 있는 상품 수량 변경
    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
        cartItemRepository.save(cartItem);
    }

    //상품 삭제 -> 체크박스 사용안하고 단품으로 삭제
       public void deleteCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }
//뷰 에서 체크박스로 선택한 상품들 모두 삭제
    public void deleteCartItemList(CartOrderOr or) {
        System.out.println("리스트 삭제");
        if (or.getCartOrderOrList().isEmpty()){
            throw new RuntimeException();
        }
        for (CartOrderOr o :or.getCartOrderOrList()){
            Long cartItemId = o.getCartItemId();
            CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
    }


    @Transactional(readOnly = true)
    public boolean checkVail(Long cartItemId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        User savedUser = cartItem.getCart().getUser();
        if (!StringUtils.equals(savedUser.getUsername(), user.getUsername())) {
            return false;
        }
        return true;
    }

    public void cartOrder(CartOrderOr cartOrderOrs, String username) throws Exception {
        System.out.println("주문서비스");

        //given -order객체 생성
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Order order = new Order(user, OrderStatus.SOLD);
        //when - 넘어온 데이터에서 상품과 갯수를 구해 orderitem객체 생성 후 db에 저장
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartOrderOr or: cartOrderOrs.getCartOrderOrList()){
            CartItem cartItem = cartItemRepository.findById(or.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            Item item=cartItem.getItem();
            int count = cartItem.getCount();
            OrderItem orderItem = OrderItem.createOrderItem(item,count,order);
            orderItemList.add(orderItem);
        }
        order.addTotalPrice(orderItemList);
        order.setOrderItems(orderItemList);
        orderRepository.save(order);
        //then - 장바구니에서 주문한 상품 삭제하기
        for (CartOrderOr or: cartOrderOrs.getCartOrderOrList()){
            CartItem cartItem = cartItemRepository.findById(or.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }


 /*       List<OrderDto> orderDtos = new ArrayList<>();
      for (CartOrderOr or :cartOrderOrs){
            Long id = or.getCartItemId();
            CartItem cartItem = cartItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            Long itemId = cartItem.getItem().getId();
            int count = cartItem.getCount();
            OrderDto orderDto = new OrderDto();
            orderDto.setCount(count);
            orderDto.setItemId(itemId);
            orderDtos.add(orderDto);
        }
        orderService.getCartOrder(orderDtos, username);

        for (CartOrderOr or :cartOrderOrs){
            Long id = or.getCartItemId();
            CartItem cartItem = cartItemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }*/

    }
}