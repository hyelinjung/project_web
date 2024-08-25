package shoppingMall.project.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LikesDto {
    private Long likedId;
    private String itemNm;
    private String imgUrl;
    private int price;
    private LocalDateTime date;
    private int discountedPrice;
    private String discount_yn;
    private Long itemId;

    public LikesDto(Long likedId, String itemNm, String imgUrl, int price, LocalDateTime date, int discountedPrice, String discount_yn, Long itemId) {
        this.likedId = likedId;
        this.itemNm = itemNm;
        this.imgUrl = imgUrl;
        this.price = price;
        this.date = date;
        this.discountedPrice = discountedPrice;
        this.discount_yn = discount_yn;
        this.itemId = itemId;
    }
}
