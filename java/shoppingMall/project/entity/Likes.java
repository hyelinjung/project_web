package shoppingMall.project.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "likes_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;
    private LocalDateTime regDateTime;

    public static Likes createLike(Item item, User user){
        Likes like = new Likes();
        like.setUser(user);
        like.setItem(item);
        like.setRegDateTime(LocalDateTime.now());
        return like;
    }
}
