/*
package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.OrderHistoDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.service.ItemService;
import shoppingMall.project.service.OrderService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    ItemService itemService;
    @Autowired
    OrderService orderService;

    @GetMapping("/search")
    @ResponseBody
    public Page<MainDto> test(@RequestBody SearchDto dto, Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,5);
        Page<MainDto> dtos = itemService.mainPageWsearch(dto,pageable);
        return dtos;
    }

    @GetMapping("/")
    @ResponseBody
    public  Page<MainDto> index(@RequestBody SearchDto dto, Optional<Integer> page, Model model){
        System.out.println("메인페이지");
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,5);
        Page<MainDto> mainDtos = itemService.mainPageWsearch(dto,pageable);
        System.out.println(mainDtos);
        return mainDtos;
    }

   */
/* @GetMapping(value = {"/orders", "/orders/{page}"})
    @ResponseBody
    public void orderHistPage(@PathVariable("page") Optional<Integer> page, Model model, Principal principal){
        System.out.println("주문 리스트");
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 1,5);
        try {
            Page<OrderHistoDto> orderHistoDtos = orderService.getOrderList(principal.getName(),pageable);
            System.out.println("order:" + orderHistoDtos);
        }catch (Exception e){
            System.out.println("error: " + e.getMessage());
        }
    }*//*

    @PostMapping("/order/cancel/{orderId}")
    public String cancelOrder(@PathVariable("orderId")Long orderId, Principal principal){
        System.out.println("주문 취소");
        if (!orderService.checkVail(principal.getName(),orderId)){
            return "권한없어";
        }
        orderService.cancelOrder(orderId);
        return "주문취소";
    }


}
*/
