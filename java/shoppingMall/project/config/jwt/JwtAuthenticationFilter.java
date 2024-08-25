package shoppingMall.project.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import shoppingMall.project.config.jwt.JwtTokenProvider;


import java.io.IOException;

//토큰필터. 모든 필터 중 가장 먼저 탐. Access토큰은 해더로 받고, refresh토큰은 cookie로 받는다 -> 인증해더로 안받고 쿠키를 받아서 사용
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String PRE="Bearer";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //String rowToken = request.getHeader("Authorization");
        String token = cookieToToken(request.getCookies());
        //String token = checkToken(rowToken);
        if (jwtTokenProvider.validToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        doFilter(request,response,filterChain);
    }
    private String checkToken(String rowToken){
        if (rowToken !=null && rowToken.startsWith(PRE)){
            return rowToken.substring(PRE.length());
        }
        return null;
    }
    private String cookieToToken(Cookie[] cookies){
        System.out.println("쿠키 들어감!!!!");
        if  (cookies == null){
            System.out.println("COOKIE IS NULL !!!");
            return null;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("access_token")){
                return cookie.getValue();
            }
        }
        return null;
    }

    /*@Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //헤더에서 jwt를 받아옵니다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        System.out.println("토큰 값 :" + token);
        if (token != null && jwtTokenProvider.validateToken(token) && token != "") {
            try {
                //토큰이 유효하면 코튼으로부터 유저 정보를 받아옵니다.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                //securityContext 에 authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (IllegalArgumentException e) {
                logger.error("an error occured during getting username from token", e);
            } catch (ExpiredJwtException e) {
                logger.warn("the token is expired and not valid anymore", e);
            } catch (SignatureException e) {
                logger.error("Authentication Failed. Username or Password not valid.");
            }

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }*/


}