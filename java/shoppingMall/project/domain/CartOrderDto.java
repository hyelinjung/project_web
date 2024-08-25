package shoppingMall.project.domain;

import jakarta.validation.constraints.Min;
import lombok.Data;


//해당상품 장바구니에 담는 dto
@Data
public class CartOrderDto {
    private Long itemId;
    @Min(value = 1,message = "최소 1개 이상 선택가능 합니다.")
    private int count;
}
