package shoppingMall.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shoppingMall.project.domain.CateDto;
import shoppingMall.project.domain.LikeServeDto;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.domain.SearchDto;
import shoppingMall.project.entity.Item;


import java.util.List;

public interface CustomRepository {

    //상품관리자페이지
    Page<Item> getAdminItemWithPage(SearchDto dto, Pageable pageable);

    //메인화면
    Page<MainDto> getMainItemWithPage(SearchDto dto,Pageable pageable);
    //전체상품 검색 +sm이 기본값이 아닐때(sm==1)
    Page<MainDto> getSearchItemWithPage(SearchDto dto,Pageable pageable);
    //카테고리별 상품 +sm 기본값
    Page<MainDto> getCateItemWithPage(CateDto cateDto,SearchDto dto, Pageable pageable);
    List<Item> getLike(CateDto cateDto, SearchDto dto,Long id);
    //카테고리별 상품 +sm 기본값 아님
    //Page<MainDto> getCateItemWithPage2(String largeItemType,String subItemType,String sm,Pageable pageable);

    long getCateItemCount(CateDto cateDto); //총갯수- 카테고리
    long getSearchItemCount(SearchDto dto); //총갯수- 카테고리
}
