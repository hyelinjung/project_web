package shoppingMall.project.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import shoppingMall.project.domain.*;
import shoppingMall.project.entity.Item;
import shoppingMall.project.service.ItemService;
import shoppingMall.project.service.ReviewService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Controller
@RequiredArgsConstructor
public class ItemViewController {
    private final ItemService itemService;
    private final ReviewService reviewService;
    @GetMapping("/admin/item/new")
    public String itemsReg(Model model){
        System.out.println("아이템 등록");
        model.addAttribute("itemDto",new ItemDto());
        return "itemNew";
    }



    //관리자 상품 상세 보기
    @GetMapping("/admin/item/{id}")
    public String itemsDtl(@PathVariable("id")Long id, Model model){
        try {
            ItemDto dto = itemService.itemDtl(id);
            System.out.println("상품 상세 보기:"+ dto);
            model.addAttribute("itemDto",dto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지않는 상품입니다.");
            model.addAttribute("itemDto",new ItemDto());
            return "itemNew";
        }
        return "itemNew";
    }



    //상품 관리자 페이지
    //검색 조건(등록일,판매상태,상품명)
    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    public String itemsPageWsearch(SearchDto dto, Model model,
                                   @PathVariable("page") Optional<Integer> page){
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,5);

        Page<Item> items = itemService.searchWithItemPage(dto,pageable);

        model.addAttribute("items",items);
        model.addAttribute("searchDto",dto);
        model.addAttribute("maxPage",5);

        return "itemMg";
    }
    //상품상세보기 -> 관련있는 상품 포함(같은 성별 카테고리 끼리)
    @GetMapping("/item/{id}")
    public String itemDtl(@PathVariable("id")Long id, Model model,Optional<Integer> page){
        System.out.println("아이템 디테일");
        ItemDto itemDto = itemService.itemDtl(id); //상품 상세
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,3);
        Page<ReviewDto> reviewDtos = reviewService.reviewDtos(id,pageable); //리뷰
        Long count = reviewService.totalcount(id);
        List<MainDto> relateDto = itemService.findRelate(id); //관련된 상품
        System.out.println(reviewDtos);
        model.addAttribute("itemDto",itemDto);
        model.addAttribute("revDto",reviewDtos);
        model.addAttribute("recount",count);
        model.addAttribute("maxPage",5);
        model.addAttribute("relateDto",relateDto);

        return "shop-details";

    }
    //전체상품 카테고리별로 보기 + 여성용/남성/키즈 & 아우터/상의/하의/원피스/악세서리 + 정렬(가격순,등록순)
    @GetMapping("/item/cate")
    public String itemCate(CateDto cateDto,SearchDto dto, Optional<Integer> page, Model model,Principal principal){
        //String temp ="abc@naver.com";
        System.out.println("카테고리");
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,18);
       /* if (!StringUtils.hasLength(cateDto.getSubItemType())){ //null값으로 들어오면 ""로 바꿈
            cateDto.setSubItemType("");
        }*/
        Page<MainDto> mainDtos = itemService.itemsCate( cateDto,dto,pageable);
        int count = itemService.getCount(cateDto);
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            try {
                String name = SecurityContextHolder.getContext().getAuthentication().getName();
                List<LikeServeDto> list = itemService.serveDtos(cateDto, dto, name);
                model.addAttribute("like", list);
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        /*if (sm != null){
            if (sm.equals("1"))
                mainDtos = itemService.itemsCate2(cateDto.getLargeItemType(),cateDto.getSubItemType(),sm,pageable);
        }*/

        System.out.println("cate:"+ mainDtos);
        System.out.println("category"+ cateDto);
        model.addAttribute("item",mainDtos);
        model.addAttribute("cateDto",cateDto); //카테고리 정보
        model.addAttribute("SearchDto",dto); //정렬 정보
        model.addAttribute("count",count);
        model.addAttribute("maxPage",10);

        return "cate";
    }

    //상품 전체 검색 - 전체 상품 수량,정렬(신상품(디폴트),낮은 가격,높은 가격,리뷰 수,별점 수,인기상품) -> 수정해야함sm 없애야함
    @GetMapping("/item/search")
    public String searchItemCon(SearchDto dto, Optional<Integer> page, Model model,CateDto cateDto, Principal principal){
        System.out.println("검색");
        System.out.println(dto);
        //String temp ="abc@naver.com";
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0,18);
        Page<MainDto> mainDtos = itemService.itemSearch(dto,pageable);
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            try {
                List<LikeServeDto> list = itemService.serveDtos(cateDto, dto, name);
                model.addAttribute("like", list);
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("main:"+ mainDtos);
        int count = itemService.getSearchCount(dto);
        /*if (sm.equals("1")){
            System.out.println("검색 들어옴");
            mainDtos =itemService.mainPageWsearch(dto,pageable);
        }*/

        model.addAttribute("item",mainDtos);
        model.addAttribute("searchDto",dto);
        model.addAttribute("count",count);
       /* model.addAttribute("sm",sm);*/
        model.addAttribute("maxPage",5);

        return "search";
    }
}
