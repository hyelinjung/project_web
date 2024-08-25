package shoppingMall.project.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

//메인화면 상품가져오는 DTO
@Data
@NoArgsConstructor
public class MainDto {
    private Long id;
    private String itemNm;
    private int price;
    private String imgUrl;
    private String itemDtl;
    private String sex;
    private Long likedId;
    private double discount;
    private int discountedPrice;
    private String discount_yn;
    @QueryProjection
    public MainDto(Long id,String imgUrl,Integer price,String itemNm,String itemDtl,String sex){
        this.id=id;
        this.imgUrl=imgUrl;
        this.price =price;
        this.itemNm = itemNm;
        this.itemDtl = itemDtl;
        this.sex=sex;
    }
    @QueryProjection
    public MainDto(Long id, String itemNm, int price, String imgUrl, String itemDtl, String sex, double discount, int discountedPrice,String discount_yn) {
        this.id = id;
        this.itemNm = itemNm;
        this.price = price;
        this.imgUrl = imgUrl;
        this.itemDtl = itemDtl;
        this.sex = sex;
        this.discount = discount;
        this.discountedPrice = discountedPrice;
        this.discount_yn = discount_yn;
    }
}
