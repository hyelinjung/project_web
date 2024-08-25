package shoppingMall.project.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;
import shoppingMall.project.StockError;

@Data
@Entity
@Table(name="cart_item")
public class CartItem { //(설정)장바구니에 담은 상품도 주문을 전재로 보고 재고에서 카운트한다 -> (1차변경)카운트 안함 변경예정!!
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_Item_id")
    private Long id;
    private int count; //갯수
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;


    //장바구니 상품 객체 생성 + 해당 상품 제고처리
    public static CartItem createCartItem(Item item,int count,Cart cart){
       CartItem cartItem =new CartItem();
       cartItem.setCart(cart);
       cartItem.setItem(item);
       cartItem.setCount(count);
       item.minStock(count);
       return cartItem;
    }
    public void addCount(int count){
        this.item.minStock(count);//추가하는 만큼 재고 처리
        this.count+=count;
    }
    //장바구니 상품 수량변경
    public void updateCount(int count){
        if (this.item.getStock()<count){ throw new StockError("재고가 부족합니다.");}
        this.item.minStock(count);
        this.count = count;
    }
}
