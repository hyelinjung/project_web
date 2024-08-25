package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shoppingMall.project.domain.OrderHistoDto;
import shoppingMall.project.service.OrderService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Controller
public class OrderViewController {
    @Autowired
    OrderService orderService;

    //그 상품 클릭 시 구매한 상품 리스트 페이지로 넘어감
    @GetMapping("/user/order/{orderId}")
    public String orderItemL(@PathVariable("orderId")Long id, Model model, Principal principal){
        System.out.println("주문 상품 컨트롤러!!!");
        //String temp ="abc@naver.com";
        /*if (!orderService.checkVail(principal.getName(),id)){*/
        if (!orderService.checkVail(principal.getName(), id)){
             model.addAttribute("errorMessage","권한이없습니다");
            return "shoping-order-detail.html";
        }
        List<OrderHistoDto> dto = orderService.orderDetl(id);
        int price = orderService.totalOrderItemListPrice(id);
        model.addAttribute("getTotalPrice",price);
        model.addAttribute("orderItem",dto);
        return "shoping-order-detail.html";
    }

    //주문한 상품 리스트 페이지 -> 구매한 것들 중 대표 상품 한 개만 표시
    @GetMapping("/user/orders")
    public String orderHistPage(Optional<Integer> page, Model model, Principal principal){
        System.out.println("주문페이지");
        //String temp ="abc@naver.com";
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,4);
        try {
            Page<OrderHistoDto> orderHistoDtos = orderService.getOrderList(principal.getName(),pageable);
            //Page<OrderHistoDto> orderHistoDtos = orderService.getOrderList(temp,pageable);
            System.out.println("주문내역:"+ Arrays.toString(orderHistoDtos.get().toArray()));
            model.addAttribute("orders",orderHistoDtos);
            model.addAttribute("page",pageable.getPageNumber());
            model.addAttribute("maxPage",5);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "shoping-order";
        }
        return "shoping-order";
    }
}
