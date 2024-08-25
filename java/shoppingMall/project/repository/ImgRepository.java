package shoppingMall.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.project.entity.ItemImg;

import java.util.List;

public interface ImgRepository extends JpaRepository<ItemImg,Long> {

     List<ItemImg> findByItemId(Long id); //상품 상세

    //주문 시 대표 사진 필요
     ItemImg findByItemIdAndImgYn(Long itemId,String imgYn);
}
