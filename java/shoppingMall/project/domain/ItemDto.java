package shoppingMall.project.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.modelmapper.ModelMapper;
import shoppingMall.project.constant.SellStatus;
import shoppingMall.project.entity.Item;


import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDto {
    private Long id;
    @NotBlank(message = "필수 입력값")
    private String itemNm;
    @NotNull(message = "재고는 필수 입력값")
    private Integer stock;
    @NotBlank(message = "상세명은 필수 입력값")
    private String itemDtl;
    @NotNull(message = "가격은 필수 입력값")
    private Integer price;
    @NotNull(message = "상품 상태는 필수로 지정해야합니다")
    private SellStatus sellStatus;
    @NotBlank(message = "하위 카테고리는 필수입니다")
    private String largeItemType; //O,T,P,D,ACT
    private String subItemType; //
    //private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(5);
    private Long[] imgId; //이미지 수정 시
    @NotBlank(message = "상위 카테고리는 필수입니다")
    private String sex;
    private double discount;
    private int discountedPrice;
    private String discount_yn;

   // private static ModelMapper modelMapper = new ModelMapper();
  /*  public  Item doe(ItemDto itemDto){
        return modelMapper.map(itemDto, Item.class);
    }
    public static ItemDto etd(Item item){
        return modelMapper.map(item, ItemDto.class);
    }*/

}
