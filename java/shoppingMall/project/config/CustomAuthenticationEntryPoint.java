package shoppingMall.project.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
//인증되지 않은 사용자 접근시 handler
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        if ("XMLHttpRequest".equals(request.getHeader("x-requested-with"))){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }else{
            response.sendRedirect("/loginForm");
        }
    }
}