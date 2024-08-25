package shoppingMall.project.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shoppingMall.project.domain.CartOrderOr;
import shoppingMall.project.domain.LikesDto;
import shoppingMall.project.repository.UserRepository;
import shoppingMall.project.service.LikesService;
import shoppingMall.project.service.UserService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class LikesController {
    @Autowired
    LikesService likesService;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;


    //관심상품 등록 -이미 등록한 상품 클릭 시 빈하트로 변하면서 리스트에서 사라짐
    @PostMapping("/user/likes/{itemId}")
    public @ResponseBody ResponseEntity<String> addLikeCon(@PathVariable("itemId") Long id, Principal principal){
        System.out.println("좋아요");
        //String temp ="abc@naver.com";
      /*  if (!likesService.checkLikes(id, principal.getName())){*/
        try {
            if (likesService.checkLikes(id, principal.getName())){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            likesService.addLikes(id, principal.getName());
            //likesService.addLikes(id, temp);
        }catch (Exception e){
            if (e.equals(new EntityNotFoundException())){
                return new ResponseEntity<>("회원만 가능", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("S", HttpStatus.OK);
    }
    @GetMapping("/user/likePage")
    public String likeListCon(Principal principal, Model model){
        System.out.println("좋아요 리스트");
        //String temp ="abc@naver.com";
        List<LikesDto> likesDtoList = likesService.likesDtos(principal.getName());
        model.addAttribute("likes",likesDtoList);
        return "likes";
    }
    @DeleteMapping("/user/likes/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteLikeCon(@PathVariable("id") Long id,Principal principal){
        System.out.println("좋아요 삭제");
        //String temp ="abc@naver.com";
        if (!likesService.deleteLikes(id, principal.getName())){
            return new ResponseEntity<>("오류가 발생해 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("선택한 상품이 삭제되었습니다", HttpStatus.OK);
    }


// 개당 상품 장바구니 담기
@PostMapping("/user/likes/cart/{itemId}")
public @ResponseBody ResponseEntity<String> addLikeCart(@PathVariable("itemId") Long id, Principal principal){
    System.out.println("장바구니");
    //String temp ="abc@naver.com";
    /*  if (!likesService.checkLikes(id, principal.getName())){*/
   likesService.likeCart(id, principal.getName());
    return new ResponseEntity<>("해당상품이 장바구니에 담겼습니다", HttpStatus.OK);
}

    // 개당 상품 주문
    @PostMapping("/user/likes/order/{itemId}")
    public @ResponseBody ResponseEntity<String> likeOrder(@PathVariable("itemId") Long id, Principal principal){
        System.out.println("장바구니");
        //String temp ="abc@naver.com";
        /*  if (!likesService.checkLikes(id, principal.getName())){*/
        likesService.likeOrder(id, principal.getName());
        return new ResponseEntity<>("해당상품을 주문했습니다", HttpStatus.OK);
    }


    //좋아요 상품 리스트 삭제
    @DeleteMapping("/user/likes/delete")
    @ResponseBody
    public ResponseEntity<String> deleteLikeList(@RequestBody CartOrderOr cartOrderOrList, Principal principal){
        System.out.println("좋아요 삭제");
        //String temp ="abc@naver.com";
        //Long id = cartOrderOrList.getCartOrderOrList().get(0).getCartItemId();
        if (!likesService.deleteList(cartOrderOrList, principal.getName())){
            return new ResponseEntity<>("오류가 발생해 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("선택한 상품이 삭제되었습니다", HttpStatus.OK);
    }
    //좋아요 상품 리스트 주문
    @PostMapping("/user/likes/order")
    public @ResponseBody ResponseEntity<String> addLikeOrderList(@RequestBody CartOrderOr cartOrderOrList, Principal principal){
        System.out.println("리스트 주문");
        //String temp ="abc@naver.com";
        /*  if (!likesService.checkLikes(id, principal.getName())){*/
        if (cartOrderOrList.getCartOrderOrList().isEmpty()){ //주문 상품 선택해야지만 주문할수 있도록 수전 완.
            return new ResponseEntity<>("주문할 상품을 선택해주세요", HttpStatus.BAD_REQUEST);
        }
        try {
            likesService.likeOrderList(cartOrderOrList, principal.getName());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("해당상품을 주문했습니다", HttpStatus.OK);
    }

    // 좋아용 상품 리스트 장바구니
    @PostMapping("/user/like/cart")
    public @ResponseBody ResponseEntity<String> likeCartList(@RequestBody CartOrderOr cartOrderOrList, Principal principal){
        System.out.println("장바구니");
        //String temp ="abc@naver.com";
        System.out.println(Arrays.toString(cartOrderOrList.getCartOrderOrList().toArray()));
        /*  if (!likesService.checkLikes(id, principal.getName())){*/
        try {
            likesService.likeCartList(cartOrderOrList, principal.getName());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("해당상품을 주문했습니다", HttpStatus.OK);
    }
}
