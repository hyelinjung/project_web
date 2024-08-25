package shoppingMall.project.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//jwt의 생성을 위한 파일값을 파라미터로 받기
@Getter
@Component
public class JwtProperties {
    @Value("${jwt.issuer}")
    String issuer ;
    @Value("${jwt.secret_key}")
    String secret_key;
}
