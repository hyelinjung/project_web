package shoppingMall.project.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Component
//토큰을 생성하고 검증하는 클래스
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    public String generatedToken(User user, Duration expire){
        Date date = new Date();
        return createToken(user,new Date(date.getTime()+expire.toMillis()));
    }

    //토큰 생성
    private String createToken(User user,Date expire){
        System.out.println("찐 토큰 생성중...");
        Date date = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(date)
                .setExpiration(expire)
                .setSubject(user.getUsername())
                .claim("id",user.getId())
                .signWith(SignatureAlgorithm.HS256,jwtProperties.getSecret_key())
                .compact();
    }
    public boolean validToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret_key())
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            System.out.println("토큰 유효기간 지남");
            return false;
        }

    }
    public Authentication getAuthentication(String token){
        Claims claims = getClaims(token);
        String username = claims.getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        String role = String.valueOf(user.getRole());
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role));
        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(),"",authorities),token,authorities);

    }
    private Claims getClaims(String token){
       return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret_key())
                .parseClaimsJws(token)
                .getBody();
    }
}

