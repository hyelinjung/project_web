package shoppingMall.project.config.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
import java.util.Map;

//OAuth2 사용하는 로그인 성공 시 -> 최종 쿠키로 전달!
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
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
        String name ="";
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String,Object> attributes = oAuth2User.getAttributes();
        if ( oAuth2User.getAttributes().containsKey("response") ){
            Map<String,Object> cop = (Map<String, Object>) attributes.get("response");
            name =(String) cop.get("name");
            System.out.println(name);
        }else {
            name = (String) attributes.get("email");
        }
        System.out.println("아이디:"+name);
        User user = userService.findUsername(name); //프로젝트 내에서 아이디 -> 아이디는 애매해서 이메일로 검색!

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
  /*  private String getTargetUrl(String accessToken){
        return UriComponentsBuilder.fromUriString(PATH)
                .queryParam("token",accessToken)
                .build()
                .toUriString();
    }*/
}
