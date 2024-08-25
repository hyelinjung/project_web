package shoppingMall.project.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

//장바구니 상품 리스트 가져오는 DTO
@NoArgsConstructor
@Data
public class CartDetailDto {
    private Long cartItemId;
    private String itemNm;
    private String itemDtl;
    private int price;
    private int count;
    private String imgUrl;
    private int discountedPrice;
    private String discount_yn;
    private Long itemId;

    public CartDetailDto(Long cartItemId, String itemNm, String itemDtl,
                         int price, int count, String imgUrl, int discountedPrice, String discount_yn, Long itemId) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.itemDtl = itemDtl;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
        this.discountedPrice = discountedPrice;
        this.discount_yn = discount_yn;
        this.itemId = itemId;
    }
}
