package shoppingMall.project.domain;

import lombok.Data;

//장바구니 에서 상품 속성 택하고 서버로 전달
@Data
public class CartAtrDto {
    private String color;
    private String size;
    private int count;
}
