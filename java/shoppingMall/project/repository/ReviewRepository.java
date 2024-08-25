package shoppingMall.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.project.domain.ReviewDto;
import shoppingMall.project.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select new shoppingMall.project.domain.ReviewDto(i.id, im.imgUrl, i.context, i.star, i.user.username) "+
            "from Review i left join RevImg im "+
            "on i.id = im.review.id "+
            "where i.item.id = :id "+
            "order by i.regDateTime desc")
    List<ReviewDto> findReview(@Param("id") Long id, Pageable page);

    @Query("select count(*) from Review q " +
            "where q.item.id = :id")
    Long findReviewCount(@Param("id") Long id);
}
