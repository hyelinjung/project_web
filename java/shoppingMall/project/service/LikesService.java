package shoppingMall.project.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shoppingMall.project.constant.OrderStatus;
import shoppingMall.project.domain.CartOrderDto;
import shoppingMall.project.domain.CartOrderOr;
import shoppingMall.project.domain.LikesDto;
import shoppingMall.project.domain.OrderDto;
import shoppingMall.project.entity.*;
import shoppingMall.project.repository.ItemRepository;
import shoppingMall.project.repository.LikesRepository;
import shoppingMall.project.repository.OrderRepository;
import shoppingMall.project.repository.UserRepository;


import java.util.ArrayList;
import java.util.List;

@Service
public class LikesService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LikesRepository  likesRepository;
    @Autowired
    UserService userService;
    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    //찜 상품으로 등록
    public void addLikes(Long itemId,String username){
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Likes likes = Likes.createLike(item,user);
        likesRepository.save(likes);
    }


    //이미 찜 한 상품인지 확인
    public boolean checkLikes(Long itemId,String username){
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Likes likes = likesRepository.findByItemIdAndUserId(itemId,user.getId());
        if (likes != null){
            likesRepository.delete(likes);
            return true;
        }
        return false;
    }
    public boolean deleteLikes(Long id,String username){
        Likes likes = likesRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        User user = likes.getUser();
        User user1 = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        if (user != user1){
            return false;
        }
        likesRepository.delete(likes);
        return true;
    }

    public boolean deleteList(CartOrderOr or,String username){
        List<CartOrderOr> list = or.getCartOrderOrList();
        for (CartOrderOr orderOr : list){
            Long id = orderOr.getCartItemId();
            Likes likes = likesRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            User user = likes.getUser();
            User user1 = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
            if (user != user1){
                return false;
            }
            likesRepository.delete(likes);
        }
        return true;
    }

//찜 상품 전체 페이지
    public List<LikesDto> likesDtos(String username){
        System.out.println("찜 전체페이지 서비스");
        List<LikesDto> likesDtoList = likesRepository.findLikesList(username);
        return likesDtoList;
    }

    //좋아요 상품 장바구니 담기 -> 기본 수량 1개
    public void likeCart(Long id,String username){
        Likes likes = likesRepository.findById(id).orElseThrow(EntityNotFoundException::new);
           Item item = likes.getItem();
        CartOrderDto dto = new CartOrderDto();
        dto.setCount(1);
        dto.setItemId(item.getId());
           cartService.addCartItem(dto,username);
        likesRepository.delete(likes);
    }

    //관심상품 1개 주문
    public void likeOrder(Long id,String username){
        Likes likes = likesRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Item item = likes.getItem();
        OrderDto dto = new OrderDto();
        dto.setCount(1);
        dto.setItemId(item.getId());
        try {
            orderService.getOrder(dto,username);
            likesRepository.delete(likes);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    //좋아요 상품 리스트 주문
    public void likeOrderList(CartOrderOr cartOrderOrs, String username) throws Exception {
        System.out.println("좋아요 주문서비스");

        //given -order객체 생성
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        Order order = new Order(user, OrderStatus.SOLD);
        //when - 넘어온 데이터에서 상품과 갯수를 구해 orderitem객체 생성 후 db에 저장
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartOrderOr or : cartOrderOrs.getCartOrderOrList()) {
            Likes likes = likesRepository.findById(or.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            Item item = likes.getItem();
            OrderItem orderItem = OrderItem.createOrderItem(item, 1, order);
            orderItemList.add(orderItem);
        }
        order.addTotalPrice(orderItemList);
        order.setOrderItems(orderItemList);
        orderRepository.save(order);
        //then - 관심상품에서 주문한 상품 삭제하기
        for (CartOrderOr or : cartOrderOrs.getCartOrderOrList()) {
            Likes likes = likesRepository.findById(or.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            likesRepository.delete(likes);
        }
    }

    //관심상품 리스트 장바구니 담기
    public void likeCartList(CartOrderOr cartOrderOrs, String username) throws Exception {
        System.out.println("좋아요 주문서비스");
        for (CartOrderOr or: cartOrderOrs.getCartOrderOrList()){
            System.out.println("!!!");
            Likes likes = likesRepository.findById(or.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            Item item = likes.getItem();
            CartOrderDto dto = new CartOrderDto();
            dto.setItemId(item.getId());
            dto.setCount(1);
            cartService.addCartItem(dto,username);
            likesRepository.delete(likes);
        }
    }

}
