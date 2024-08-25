package shoppingMall.project.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.project.domain.CartDetailDto;
import shoppingMall.project.entity.Cart;
import shoppingMall.project.entity.CartItem;
import shoppingMall.project.entity.User;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query( "select "
            +"new shoppingMall.project.domain.CartDetailDto(ci.id, i.itemNm, i.itemDtl, i.price, ci.count, im.imgUrl, i.discountedPrice, i.discount_yn, i.id )"
                    +" from CartItem ci , ItemImg im "
                    +"join ci.item i "
                    +"where ci.item.id = im.item.id "
                    +"and im.imgYn = 'Y' "
                    +"and ci.cart.id = :cartId "
                    +"order by ci.item.id desc")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId); //해당 장바구니의 상품

    //해당 상품 중복
    CartItem findByCartIdAndItemId(Long cartId,Long itemId);

}
