package shoppingMall.project.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

//리뷰 view dto
@Data
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private String imgUrl;
    private String itemNm;
    private String attr; //상품 하위 속성 -> 아직 계발 안함
    private String text;
    private int star;
    private int price;
    private String name;

    public ReviewDto(Long itemId, String imgUrl, String itemNm,int price) {
        this.id = itemId;
        this.imgUrl = imgUrl;
        this.itemNm = itemNm;
        this.price=price;
    }

    //서버 -> 뷰
    public ReviewDto(Long id, String imgUrl, String text, int star, String name) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.text = text;
        this.star = star;
        this.name = name;
    }
}
