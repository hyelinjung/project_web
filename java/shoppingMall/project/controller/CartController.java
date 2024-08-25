package shoppingMall.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.domain.CartDetailDto;
import shoppingMall.project.domain.CartOrderDto;
import shoppingMall.project.domain.CartOrderOr;
import shoppingMall.project.service.CartService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    @PostMapping("/user/cart/add") //장바구니의 상품 추가
    @ResponseBody
    public ResponseEntity<String> addCartItem(@RequestBody @Valid CartOrderDto dto, BindingResult bindingResult, Principal principal){
        System.out.println("장바구니의 추가");
        //String temp ="abc@naver.com";
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors){
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
           //Long id= cartService.addCartItem(dto,principal.getName());
           cartService.addCartItem(dto,principal.getName());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("상품을 장바구니에 담았습니다.", HttpStatus.OK);
    }
    //장바구니 정보 가져오기
    @GetMapping("/user/cart")
    public String getCart(Principal principal, Model model){
        //String temp ="abc@naver.com";
        System.out.println("장바구니 페이지 가져오기");
        List<CartDetailDto> cartDetailDtos = cartService.cartItemList(principal.getName());
        //List<CartDetailDto> cartDetailDtos = cartService.cartItemList(temp);
        model.addAttribute("cartItems",cartDetailDtos);
        return "shoping-cart";
    }

    //장바구니 수량 수정하기
    @PatchMapping("/user/cart/update/{cartItemId}")
    @ResponseBody
    public ResponseEntity<String> updateCount(@PathVariable("cartItemId")Long id,Principal principal,int count){
        System.out.println("장바구니 수정");
        //String temp ="abc@naver.com";
        if (count<=0){
            return new ResponseEntity<>("최소 1개 이상 가능합니다.",HttpStatus.BAD_REQUEST);
        }
      /* if (cartService.checkVail(id, principal.getName())){*/
        if (cartService.checkVail(id, principal.getName())){
            cartService.updateCartItemCount(id,count);
        }else{
            return new ResponseEntity<>("false",HttpStatus.FORBIDDEN);
        }
          return new ResponseEntity<>("수정이 완료되었습니다.",HttpStatus.OK);

    }
    //장바구니 삭제
    @DeleteMapping("/user/cart")
    @ResponseBody
    public ResponseEntity<String> deleteCartItem(@RequestBody CartOrderOr ors,Principal principal){
        System.out.println("장바구니 상품 삭제");
        System.out.println(Arrays.toString(ors.getCartOrderOrList().toArray()));
        //String temp ="abc@naver.com";
       /* if (!cartService.checkVail(id, principal.getName())) {*/
        /*if (!cartService.checkVail(id,temp)) {
            return new ResponseEntity<>("권한이 없습니다.",HttpStatus.FORBIDDEN);
        }*/
        try{
            cartService.deleteCartItemList(ors);
        }catch (RuntimeException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("상품을 선택해주세요",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("해당 상품이 장바구니에서 삭제되었습니다.",HttpStatus.OK);
    }

    @PostMapping("/user/cart/order")
    @ResponseBody
    public ResponseEntity<String> cartOrder(@RequestBody CartOrderOr ors, Principal principal){
        System.out.println("장바구니에서 주문하기");
        //String temp ="abc@naver.com";
        List<CartOrderOr> cartOrderOrList = ors.getCartOrderOrList();
        if (cartOrderOrList.isEmpty()){
            return new ResponseEntity<>("주문할 상품을 선택해주세요",HttpStatus.BAD_REQUEST);
        }
        try {
            //cartService.cartOrder(cartOrderOrList, principal.getName());
            cartService.cartOrder(ors, principal.getName());
        }catch (Exception e){
            return new ResponseEntity<>("상품을 선택해주세요",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("주문이 완료되었습니다.", HttpStatus.OK);
    }
}
