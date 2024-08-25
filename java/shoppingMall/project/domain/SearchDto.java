package shoppingMall.project.domain;

import lombok.Data;
import shoppingMall.project.constant.SellStatus;
@Data
public class SearchDto {
    private String searchDate; // 날짜별
    private String subItemType; //검색 타입_상품 타입 A,O,T,P,AC,D

    private String searchType; //상품 관리자 검색
    private String searchBy; //검색어
    private String orderBy=""; //검색 정렬 기준 ,리뷰순, 가격순, 별점순,인기순(구매량)
    private SellStatus sellStatus;// 판매 상태
}
