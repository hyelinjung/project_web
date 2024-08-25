package shoppingMall.project.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shoppingMall.project.config.jwt.JwtTokenProvider;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

import java.time.Duration;

//엑세스 토큰이 만료됬을때 리프레시토큰으로 새로운 엑세스 토큰 생성
@Service
@RequiredArgsConstructor
public class NewCreateTokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final CheckRefreshTokenService refreshTokenService;

    public String checkOrCreate(String refreshToken){
        if (!jwtTokenProvider.validToken(refreshToken)){
            throw new RuntimeException("invalidTokenError");
        }
        Long id = refreshTokenService.checkRefreshToken(refreshToken).getUserId();
       User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return jwtTokenProvider.generatedToken(user, Duration.ofHours(2));

    }
}
