package shoppingMall.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shoppingMall.project.domain.UserDto;
@Controller
public class MemberViewController {

    @GetMapping("/loginForm")
    public String loginForm(){
       /* return "member/loginForm";*/
        return "login";
    }

    @GetMapping("/joinForm")
    public String joinForm(Model model){
        model.addAttribute("userDto",new UserDto());
        return "join";
    }
    //배포 후 수정 -> 로그인 실패시 errorMessage를 쿼리스트링으로 감아서 /loginForm로 리다이렉트
    @GetMapping("/error/login")
    public String errorlogin(Model model){
        System.out.println("에러로그인");
        model.addAttribute("errorMessage","아이디 또는 비밀번호가 틀렸습니다.");
        return "login";
    }
}
