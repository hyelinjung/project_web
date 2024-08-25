package shoppingMall.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
//리뷰 이미지 엔티티
@Entity
@Table(name = "rev_img")
@Data
public class RevImg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="revImg_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "review_id")
    private Review review;
    private String imgOriNm;
    private String imgUrl;
}
