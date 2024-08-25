package shoppingMall.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.project.domain.LikesDto;
import shoppingMall.project.entity.Item;
import shoppingMall.project.entity.Likes;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes,Long> {
    Likes findByItemIdAndUserId(Long itemId,Long memberId); //좋아요 유무 구분

    @Query("select new shoppingMall.project.domain.LikesDto(i.id, i.item.itemNm, im.imgUrl,i.item.price,i.regDateTime, i.item.discountedPrice, i.item.discount_yn, i.item.id ) "+
            "from Likes i "+
            "join ItemImg im on i.item.id = im.item.id "+
            "where i.user.username = :username and im.imgYn = 'Y' "+
            "order by i.regDateTime desc")
    List<LikesDto> findLikesList(@Param("username") String username);
}
