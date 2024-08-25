package shoppingMall.project.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void addCookie(HttpServletResponse response, HttpServletRequest request,String name,String value,int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

    }

    public static void deleteCookie(HttpServletResponse response,HttpServletRequest request,String name){
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return;
        }
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(name)){
                cookie.setPath("/");
                cookie.setMaxAge(0);
                cookie.setValue("");
                response.addCookie(cookie);
            }
        }
    }
}
