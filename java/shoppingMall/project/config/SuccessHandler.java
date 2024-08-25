package shoppingMall.project.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;
import shoppingMall.project.config.jwt.JwtTokenProvider;
import shoppingMall.project.entity.RefreshToken;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.RefreshTokenRepository;
import shoppingMall.project.service.UserService;
import shoppingMall.project.util.CookieUtil;

import java.io.IOException;
import java.time.Duration;

//시큐리티를 사용하는 로그인 성공 시 -> 최종! 쿠키로 주고받기로 함!
@RequiredArgsConstructor
public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final Duration REFRESH_TOKEN_COOKIE = Duration.ofDays(14);
    public static final String REFRESH_TOKEN_NAME = "refresh_token";
    public static final String ACCESS_TOKEN_NAME = "access_token";
    public static final String PATH ="/";

    private final JwtTokenProvider provider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("일반로그인 성공핸들러"+userDetails.getUsername());
        User user = userService.findUsername(userDetails.getUsername()); //프로젝트 내에서 아이디
        System.out.println("토큰 생성중...");
        String refreshToken = provider.generatedToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshToken(request, response, refreshToken);

        String accessToken = provider.generatedToken(user, ACCESS_TOKEN_DURATION);
        //String targetUrl = getTargetUrl(accessToken);
        addAccessToken(request,response,accessToken);

        //getRedirectStrategy().sendRedirect(request, response, targetUrl);
        response.sendRedirect("/");
    }

    private void addRefreshToken(HttpServletRequest request,HttpServletResponse response,String refreshToken){
        CookieUtil.deleteCookie(response,request,REFRESH_TOKEN_NAME);
        CookieUtil.addCookie(response,request,REFRESH_TOKEN_NAME,refreshToken, (int) REFRESH_TOKEN_COOKIE.toSeconds());
    }

    private void addAccessToken(HttpServletRequest request,HttpServletResponse response,String accessToken){
        CookieUtil.deleteCookie(response,request,ACCESS_TOKEN_NAME);
        CookieUtil.addCookie(response,request,ACCESS_TOKEN_NAME,accessToken, (int) REFRESH_TOKEN_COOKIE.toSeconds());
    }
    private void saveRefreshToken(Long id,String refreshToken){
        RefreshToken token =refreshTokenRepository.findByUserId(id)
                .map(entity -> entity.update(refreshToken))
                .orElse(new RefreshToken(id,refreshToken));
        refreshTokenRepository.save(token);
    }
   /* private String getTargetUrl(String accessToken){
        return UriComponentsBuilder.fromUriString(PATH)
                .queryParam("token",accessToken)
                .build()
                .toUriString();
    }*/
}
