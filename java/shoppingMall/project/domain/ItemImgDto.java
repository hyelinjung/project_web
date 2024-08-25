package shoppingMall.project.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import shoppingMall.project.entity.ItemImg;

@Data
@NoArgsConstructor
public class ItemImgDto {
    private Long id;
   private String itemOriginNm; //원본 제목
    private String imgNm; //uui+ 제목
    private String imgYn; // 대표 사진
    private String itemUrl;
    //private static ModelMapper modelMapper =new ModelMapper();

    /*public static ItemImgDto eTd(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class);
    }*/

    public ItemImgDto(Long id, String itemOriginNm, String imgYn, String itemUrl) {
        this.id = id;
        this.itemOriginNm = itemOriginNm;
        this.imgYn = imgYn;
        this.itemUrl = itemUrl;
    }
}
