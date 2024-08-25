package shoppingMall.project.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

//상품페이지에서 주문 시
@Data
public class OrderDto {

    private Long itemId;
    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다.")
    private int count;
}
