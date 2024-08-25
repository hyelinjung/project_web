package shoppingMall.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import shoppingMall.project.config.jwt.JwtAuthenticationFilter;
import shoppingMall.project.config.jwt.JwtTokenProvider;
import shoppingMall.project.config.oauth2.Oauth2Service;
import shoppingMall.project.config.oauth2.Oauth2SuccessHandler;
import shoppingMall.project.repository.RefreshTokenRepository;
import shoppingMall.project.service.UserService;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final Oauth2Service oauth2Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public BCryptPasswordEncoder cryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer configure(){ //스프링 시큐리티 기능 비활성화
        return (web)-> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers(
                        new AntPathRequestMatcher("/static/img/**"),
                        new AntPathRequestMatcher("/static/css/**"),
                        new AntPathRequestMatcher("/static/fonts/**"),
                        new AntPathRequestMatcher("/images/**"),
                        new AntPathRequestMatcher("/static/js/**"),
                        new AntPathRequestMatcher("/static/vendor/**"),
                        new AntPathRequestMatcher("/static/js/**")
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http //토큰 방식으로 인증 하기 때문에 기존 사용하던 폼 로그인, 세션 비활성화
                .csrf(AbstractHttpConfigurer::disable)


                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth->
                        auth.requestMatchers(new AntPathRequestMatcher("/api/token")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasAnyRole("ADMIN","USER")
                                .requestMatchers(new AntPathRequestMatcher("/user/**")).authenticated()
                                .anyRequest().permitAll())
                .exceptionHandling(ex->
                        ex.accessDeniedHandler(new WebAccessDeniedHandler())
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .formLogin(form->form.loginPage("/login")
                        .successHandler(successHandler())
                        //.failureUrl("/error/login"))
                        .failureHandler(new LoginFailureHandler()))
                .oauth2Login(ou->
                        ou
                                .userInfoEndpoint(endpoint->endpoint.userService(oauth2Service))
                                .successHandler(oauth2SuccessHandler())
                )

                .build();
    }
    @Bean
    public JwtAuthenticationFilter tokenAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }
    @Bean
    public SuccessHandler successHandler(){
        return new SuccessHandler(jwtTokenProvider,userService,refreshTokenRepository);
    }

    @Bean
    public Oauth2SuccessHandler oauth2SuccessHandler(){
        return new Oauth2SuccessHandler(jwtTokenProvider,userService,refreshTokenRepository);
    }
}
