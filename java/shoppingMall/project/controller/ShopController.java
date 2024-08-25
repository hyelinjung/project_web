package shoppingMall.project.controller;

import jakarta.persistence.EntityNotFoundException;
import org.eclipse.jdt.internal.compiler.batch.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shoppingMall.project.domain.CateDto;
import shoppingMall.project.domain.LikeServeDto;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.service.ItemService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {
    @Autowired
    ItemService itemService;

    //구현 내용 -> 모든 상품, 인기많은 상품순, 관심상품 표시
    @GetMapping("/")
    public String index(SearchDto dto,  Model model, CateDto cateDto){
        System.out.println("메인페이지");
        System.out.println(dto);
        //String temp ="abc@naver.com";
        //Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,8);
       // Page<MainDto> mainDtos = itemService.itemSearch(dto,pageable); //일반 메인 화면 -> 검색조건에 따른 페이지는 페이지 뷰 새로 만들어야함
        List<MainDto> mainDtos = itemService.mainNewItem();
       if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
           try {
               String name = SecurityContextHolder.getContext().getAuthentication().getName();
               System.out.println("이름:"+ name);
               List<LikeServeDto> list = itemService.serveDtos(cateDto, dto, name); //해당 사용자의 관심상품 정보 가져옴
               model.addAttribute("like", list);
           } catch (EntityNotFoundException e) {
               System.out.println("shopcontroller오류발생!");
           }
       }

        List<MainDto> pop = itemService.popularItem(); //인기순 상품 8개
        /*if (dto.getSearchBy() == null){
            dto.setSearchBy("");
        }*/
        System.out.println("main:"+ Arrays.toString(mainDtos.toArray()));
        System.out.println("main2:"+ mainDtos);
        System.out.println("main:"+ Arrays.toString(pop.toArray()));
        model.addAttribute("item",mainDtos);
        model.addAttribute("searchDto",dto);
        //model.addAttribute("maxPage",30);

        model.addAttribute("pop",pop);
        return "index";
    }


}
