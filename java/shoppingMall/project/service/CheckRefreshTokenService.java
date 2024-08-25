package shoppingMall.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shoppingMall.project.entity.RefreshToken;
import shoppingMall.project.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class CheckRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    //넘어온 리프레시 토큰값이 db에 있는지 확인
    public RefreshToken checkRefreshToken(String token){
        return refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(IllegalArgumentException::new);
    }

}
