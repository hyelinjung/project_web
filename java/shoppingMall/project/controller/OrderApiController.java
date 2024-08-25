package shoppingMall.project.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.domain.OrderDto;
import shoppingMall.project.service.OrderService;

import java.security.Principal;
import java.util.List;

@Controller
public class OrderApiController {
    @Autowired
    OrderService orderService;


    @PostMapping("/user/order/item")
    @ResponseBody
    public ResponseEntity<String> getOrder(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){
        System.out.println("주문");
        //String temp ="abc@naver.com";

        if (bindingResult.hasErrors()){
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors){
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            orderService.getOrder(orderDto, principal.getName());
           // orderService.getOrder(orderDto, temp);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("주문이 완료되었습니다.",HttpStatus.OK);
    }

    @PostMapping("/user/order/cancel/{orderId}")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@PathVariable("orderId")Long orderId,Principal principal){
        System.out.println("주문 취소");
        //String temp ="abc@naver.com";
       /* if (!orderService.checkVail(principal.getName(),orderId)){*/
        if (!orderService.checkVail(principal.getName(), orderId)){
            return new ResponseEntity<>("권한이 없습니다",HttpStatus.FORBIDDEN);
        }
        try {
            orderService.cancelOrder(orderId);
        }catch (RuntimeException e){
            return new ResponseEntity<>("이미 취소된 상품입니다.",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("주문이 취소되었습니다.",HttpStatus.OK);
    }

}
