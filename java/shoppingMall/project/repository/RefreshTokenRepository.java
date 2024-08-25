package shoppingMall.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import shoppingMall.project.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {


    Optional<RefreshToken> findByUserId(Long id);
    Optional<RefreshToken> findByRefreshToken(String token);
}
