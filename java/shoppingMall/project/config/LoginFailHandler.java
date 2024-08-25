/*
package shoppingMall.project.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
@Component
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        String errorMessage = "";
        if (exception instanceof UsernameNotFoundException){
            errorMessage = "아이디 또는 비밀번호가 틀렸습니다.";
        }
        setDefaultFailureUrl("/loginForm?error=true&errorMessage=" + errorMessage);
        //super.onAuthenticationFailure(request, response, exception);
    }
}
*/
