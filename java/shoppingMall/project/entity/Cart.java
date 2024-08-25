package shoppingMall.project.entity;

import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id")
    private User user;

    public static Cart createCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }
}
