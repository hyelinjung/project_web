package shoppingMall.project.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import shoppingMall.project.service.NewCreateTokenService;
import shoppingMall.project.util.CookieUtil;

import java.time.Duration;

@Controller
@RequiredArgsConstructor
public class TokenApiController {
    private final NewCreateTokenService service;


    //리프레시 토큰으로 새 토큰 요청
    @PostMapping("/api/token")
    @ResponseBody
    public ResponseEntity<String> createNewToken(HttpServletRequest request, HttpServletResponse response){
        System.out.println("새로운 엑세스 토큰 생성 요청 컨트롤러");
        Cookie[] cookie = request.getCookies();
        for (Cookie cookie1 : cookie){
            if (cookie1.getName().equals("refresh_token")){
                String token = cookie1.getValue();
                String newAccessToken = service.checkOrCreate(token);
                CookieUtil.deleteCookie(response,request,"access_token");
                CookieUtil.addCookie(response,request,"access_token",newAccessToken, (int) Duration.ofDays(1).toSeconds());
                return new ResponseEntity<>(HttpStatus.OK);
            }

        }
        System.out.println("TOKEN COOKIE IS NULL !!!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
