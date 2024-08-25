package shoppingMall.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shoppingMall.project.domain.CateDto;
import shoppingMall.project.domain.ItemDto;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.service.ItemService;

import java.util.Optional;

@Controller
public class ItemController {
    @Autowired
    ItemService itemService;


   /* @GetMapping("/item/review/{id}")
    public ResponseEntity<String> getReview(@PathVariable Long id, Principal principal){
        //해당 상품을 구해한 이력이 있는 회원인지
        //principal.getName()에서 name은 사용자의 id값

    }*/
}
