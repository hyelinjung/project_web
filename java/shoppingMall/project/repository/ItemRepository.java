package shoppingMall.project.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shoppingMall.project.domain.MainDto;
import shoppingMall.project.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long>,CustomRepository {

     //Item findById(String id); //상품 상세

    /*@Query("select new shoppingMall.project.domain.MainDto(i.id, im.imgUrl, i.price, i.itemNm, i.itemDtl) "+
            "from Item i "+
            "join ItemImg im "+
            "on i.id = im.item.id "+
            //"where i.largeItemType = :largeItemType and i.subItemType = :subItemType "+
            "where im.imgYn = 'Y' "+
            "and i.subItemType = :subItemType "+
            "and i.largeItemType = :largeItemType "+
            "order by i.id desc")
    List<MainDto> findCate(@Param("largeItemType")String largeItemType, @Param("subItemType") String subItemType, Pageable pageable);*/

   /* @Query("select count(*) from Item i where i.largeItemType = :largeItemType and i.subItemType = :subItemType")
    int itemCount(@Param("largeItemType") String largeItemType ,@Param("subItemType") String subItemType);*/

    //상품 상세보기에서 관련있는 상품들
    @Query("select new shoppingMall.project.domain.MainDto(i.id, i.itemNm, i.price, im.imgUrl, i.itemDtl, i.sex, i.discount, i.discountedPrice,i.discount_yn) "+
            "from Item i "+
            "join ItemImg im "+
            "on i.id = im.item.id "+
            //"where i.largeItemType = :largeItemType and i.subItemType = :subItemType "+
            "where im.imgYn = 'Y' "+
            "and i.sex = :sex "+
            "order by i.id desc")
    List<MainDto> findRelate(@Param("sex")String sex);

    @Query("select new shoppingMall.project.domain.MainDto(i.id,  i.itemNm, i.price, im.imgUrl, i.itemDtl, i.sex, i.discount, i.discountedPrice,i.discount_yn) "+
            "from Item i "+
            "join ItemImg im "+
            "on i.id = im.item.id "+
            "where im.imgYn = 'Y' "+
            "order by i.popular desc "+
            "limit 8 ")
    List<MainDto> popularItem();

    //메인화면 신상품들
    @Query("select new shoppingMall.project.domain.MainDto(i.id,  i.itemNm, i.price, im.imgUrl, i.itemDtl, i.sex, i.discount, i.discountedPrice,i.discount_yn) "+
            "from Item i "+
            "join ItemImg im "+
            "on i.id = im.item.id "+
            "where im.imgYn = 'Y' "+
            "and i.sex = 'W' "+
            "order by i.regDateTime desc "+
            "limit 8 ")
    List<MainDto> mainNewItemW();
    @Query("select new shoppingMall.project.domain.MainDto(i.id,  i.itemNm, i.price, im.imgUrl, i.itemDtl, i.sex, i.discount, i.discountedPrice,i.discount_yn) "+
            "from Item i "+
            "join ItemImg im "+
            "on i.id = im.item.id "+
            "where im.imgYn = 'Y' "+
            "and i.sex = 'M' "+
            "order by i.regDateTime desc "+
            "limit 8 ")
    List<MainDto> mainNewItemM();
    @Query("select new shoppingMall.project.domain.MainDto(i.id,  i.itemNm, i.price, im.imgUrl, i.itemDtl, i.sex, i.discount, i.discountedPrice,i.discount_yn) "+
            "from Item i "+
            "join ItemImg im "+
            "on i.id = im.item.id "+
            "where im.imgYn = 'Y' "+
            "and i.sex = 'K' "+
            "order by i.regDateTime desc "+
            "limit 8 ")
    List<MainDto> mainNewItemK();
}
