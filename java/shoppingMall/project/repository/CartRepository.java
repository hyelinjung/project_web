package shoppingMall.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.project.entity.Cart;
import shoppingMall.project.entity.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
     Cart findByUserId(Long userId);
}
