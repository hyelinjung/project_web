package shoppingMall.project.controller;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shoppingMall.project.config.jwt.JwtTokenProvider;
import shoppingMall.project.domain.UserDto;
import shoppingMall.project.repository.UserRepository;
import shoppingMall.project.service.UserService;
import shoppingMall.project.util.CookieUtil;

import java.util.Random;

@Controller
public class MemberApiController {
    @Value("${spring.mail.username}")
    String form;
    @Autowired
    UserService userService;

    @Autowired
    JavaMailSender javaMailSender;

/*    @PostMapping("/login")
    public String loginForm(UserDto dto, HttpServletResponse response, HttpServletRequest request)throws NullPointerException{
        User user = userRepository.findByUsername(dto.getUsername());
        String pwd = passwordEncoder.encode(dto.getPassword());
        System.out.println("pwd:"+pwd);
        if (user == null || !passwordEncoder.matches(dto.getPassword(),user.getPassword())){
          String errorMessage ="false";
            return "redirect:/members/loginForm?errorMessage="+errorMessage;
        }
       String token = jwtTokenProvider.createToken(user.getUsername(),String.valueOf(user.getRole()));
       String refresh_token = jwtTokenProvider.create_refresh_Token(String.valueOf(user.getRole()));
        System.out.println("token:" + token);
        Cookie cookie = new Cookie("accessToken",token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Authorization",token);
        return "redirect:/";
    }*/

    //회원가입
    //아이디는 중복이면 안됨
    @PostMapping("/join")
    public String joinPost(@Valid UserDto userDto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()){ //유효성 검사
            return "join";
        }
        try{
                userService.checkUserJoin(userDto);
                System.out.println("이미 회원");
                model.addAttribute("errorMessage","중복된 아이디입니다.");
                return "join";

        }catch (IllegalStateException e){
            System.out.println("신규회원");
            userService.userJoin(userDto);
            return "redirect:/";
        }


    }
    @GetMapping("/mailCheck/{email}")
    @ResponseBody
    public String mailCheck(@PathVariable("email") String email){
        System.out.println("메일전송:"+email);

        Random random = new Random();
        int ranNum = random.nextInt(8888) + 1111;
        String num = String.valueOf(ranNum);
        System.out.println(ranNum);

        String setForm = form;
        System.out.println(setForm);
        String toMail = email;
        String title = "회원가입 인증 메일입니다.";
        String content = "프로젝트 회원 인증"+
                "<br><br> 인증번호는 "+ ranNum+"입니다."+
                "<br>"+"해당 인증번호를 인증번호 확인란에 기입해주세요";
        try {
            MimeMessage ms = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(ms,true,"utf-8");
            helper.setFrom(setForm);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            javaMailSender.send(ms);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        return num;
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity logout(HttpServletResponse response, HttpServletRequest request){
        System.out.println("로그아웃");
        CookieUtil.deleteCookie(response,request,"refresh_token");
        CookieUtil.deleteCookie(response,request,"access_token");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
