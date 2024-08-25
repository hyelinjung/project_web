package shoppingMall.project.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name="item_img")
@NoArgsConstructor
public class ItemImg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_img_id")
    private Long id;
    private String imgOriginNm; //원본명
    private String imgNm; //확장자
    private String imgUrl; //파일 리소스 url
    @Column(nullable = false)
    private String imgYn;
   @ManyToOne
   @JoinColumn(name = "item_id")
    private Item item;


   public void updateImg(String imgOriginNm,String imgUrl,String imgNm){
       this.imgNm = imgNm;
       this.imgUrl = imgUrl;
       this.imgOriginNm = imgOriginNm;
   }

    public ItemImg( String imgOriginNm, String imgNm, String imgUrl, String imgYn, Item item) {
        this.imgOriginNm = imgOriginNm;
        this.imgNm = imgNm;
        this.imgUrl = imgUrl;
        this.imgYn = imgYn;
        this.item = item;
    }


}
