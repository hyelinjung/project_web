package shoppingMall.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import shoppingMall.project.domain.ItemDto;
import shoppingMall.project.service.ItemService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemApiController {
    private final ItemService itemService;

    //상품 등록
    @PostMapping("/admin/item/new")
    public String itemsReg(@Valid ItemDto dto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> multipartFileList)throws Exception {
        System.out.println("post 아이템등록");
        if (bindingResult.hasErrors()){
            System.out.println("오류발생");
            return "itemNew";
        }
        if (multipartFileList.get(0).isEmpty()){
            model.addAttribute("errorMessage","대표사진은필수값입니다.");
            return "itemNew";
        }
        try {
            itemService.saveItem(dto,multipartFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "itemNew";
        }
        return "itemNew";
    }

    //상품수정
    @PostMapping("/admin/item/upd")
    public String itemsUpdate(@Valid ItemDto itemDto,BindingResult bindingResult,Model model,@RequestParam("itemImgFile") List<MultipartFile> multipartFileList){
        System.out.println("수정 컨트롤러");
        System.out.println("vo"+ itemDto);
        if (bindingResult.hasErrors()){
            return "itemNew";
        }
        if (multipartFileList.get(0).isEmpty() && itemDto.getId() == null){
            model.addAttribute("errorMessage","대표사진은 필수값입니다.");
            return "itemNew";
        }
        try {
            itemService.updateItem(itemDto,multipartFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage",e.getMessage());
            return "itemNew";
        }
        return "redirect:/";
    }
}
