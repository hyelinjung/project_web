package shoppingMall.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import shoppingMall.project.StockError;
import shoppingMall.project.constant.SellStatus;
import shoppingMall.project.domain.ItemDto;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Table(name="item")
public class Item /*extends BaseTimeEntity*/{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;
    @Column(nullable = false, length = 50)
    private String itemNm;
    @Lob
    @Column(nullable = false)
    private String itemDtl;
    @Column(name="price", nullable = false)
    private int price;
    @Column(nullable = false)
    private int stock;
    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus;
    private String largeItemType; //아우터, 상의,하의,원피스,악세서리
    private String subItemType;
    @Column(nullable = false)
    private String sex; //여성,남성, 키즈
    private LocalDateTime regDateTime;
    private LocalDateTime updateTime;
    private Integer popular; //사람들이 구매한 횟수 -> 아직 구현 안함-> 구현 완
    private double discount;//할인율
    private int discountedPrice;//할인된 가격
    private String discount_yn;

    public  void createItem(ItemDto itemDto){
       itemNm = itemDto.getItemNm();
       itemDtl=itemDto.getItemDtl();
       price=itemDto.getPrice();
       stock=itemDto.getStock();
       sellStatus = itemDto.getSellStatus();
       largeItemType=itemDto.getLargeItemType();
       subItemType=itemDto.getSubItemType();
        regDateTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        sex=itemDto.getSex();
        discount = itemDto.getDiscount();
        discountedPrice= itemDto.getDiscountedPrice();
        discount_yn = itemDto.getDiscount_yn();
    }

    public void updateItem(ItemDto itemDto){
        this.price = itemDto.getPrice();
        this.itemDtl = itemDto.getItemDtl();
        this.itemNm = itemDto.getItemNm();
        this.stock = itemDto.getStock();
        this.sellStatus = itemDto.getSellStatus();
        this.largeItemType =itemDto.getLargeItemType();
        this.subItemType =itemDto.getSubItemType();
        updateTime = LocalDateTime.now();
        this.sex = itemDto.getSex();
        discount = itemDto.getDiscount();
        discountedPrice= itemDto.getDiscountedPrice();
        discount_yn = itemDto.getDiscount_yn();
    }

    //재고관리
    public void minStock(int count){
        this.stock -= count;
        if (this.getStock()<count){
            throw new StockError("재고가 부족합니다.");
        }
        this.popular = count;
    }
    public void addStock(int count){
        this.stock += count;
        this.popular -=count;
    }

}
