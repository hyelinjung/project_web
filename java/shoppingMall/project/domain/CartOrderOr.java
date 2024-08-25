package shoppingMall.project.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//비동기로 장바구니에서 구매할 상품을 선택해서 서버로 보낼때
@Data
public class CartOrderOr {
    private Long cartItemId;
    private List<CartOrderOr> cartOrderOrList = new ArrayList<>(); //장바구니에 담은 상품아이디를 리스트로 보내면 매핑될 변수
}
