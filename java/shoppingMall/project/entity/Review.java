package shoppingMall.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="review_id")
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne()
    @JoinColumn(name = "member_id")
    private User user;
    private String ad1; //상품 속성 -> 아직 구현 안함
    private String ad2;
    private String ad3;

    @Column(nullable = false)
    private String context; //내용
    private LocalDateTime regDateTime;
    @Column(nullable = false)
    private int star; //별점
    @Builder
    public Review(Item item,User user,String ad1,String ad2,String ad3,String context,int star){
        this.item=item;
        this.user=user;
        this.ad1=ad1;
        this.ad2=ad2;
        this.ad3=ad3;
        this.star=star;
        this.context =context;
        this.regDateTime = LocalDateTime.now();
    }
}
