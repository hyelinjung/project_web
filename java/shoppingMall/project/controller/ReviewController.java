package shoppingMall.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.domain.ReviewDto;
import shoppingMall.project.service.OrderService;
import shoppingMall.project.service.ReviewService;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final OrderService orderService;





    //리뷰를 쓸수 있는 조건 = 구매이력이 있는 회원인가 확인
    @GetMapping("/user/review/check")
    @ResponseBody
    public ResponseEntity<String> getCheckReview(Long id, Principal principal){
        //String temp ="abc@naver.com";
        System.out.println("리뷰");

        if(orderService.checkReviewer(principal.getName(), id)) {
           return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>("해당 상품을 구매한 회원만 가능합니다.",HttpStatus.BAD_REQUEST);
        }
    }

    //리뷰 페이지
    @GetMapping("/user/review")
    public String getReview(Long id, Principal principal, Model model){
        //String temp ="abc@naver.com";
        ReviewDto dto = reviewService.getReview(id);
        System.out.println("리뷰"+dto.toString());

        if(orderService.checkReviewer(principal.getName(), id)) {
            model.addAttribute("itemDto",dto);
        }else{

        }
        return "review";
    }

    //리뷰 등록하고 해당 상품페이지로 리다이렉
    @PostMapping("/user/review")
    public String  addReview( ReviewDto dto,  Principal principal, @RequestParam("itemImgFile") MultipartFile file) throws IOException {

        //dto.setStar(star);
        System.out.println("리뷰쓰기");
        System.out.println(dto.toString());
        System.out.println(file.getOriginalFilename());
        //String temp ="abc@naver.com";

            reviewService.addReview(dto, principal.getName(), file);

        return "redirect:/";
    }

    //해당 상품의 리뷰가져오기, 정렬(별점 높은 순, 낮은순, 최신순)-> 정렬은 나중에
    @GetMapping("/user/review/page/{id}")
    public void getAllReviewPage(@PathVariable("id")Long id, Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,3);
        Page<ReviewDto> reviewDtos = reviewService.reviewDtos(id,pageable);

        System.out.println(reviewDtos);
    }
}
