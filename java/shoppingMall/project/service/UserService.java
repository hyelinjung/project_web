package shoppingMall.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shoppingMall.project.domain.UserDto;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
     private final UserRepository userRepository;



     //회원가입
    public void checkUserJoin(UserDto userDto){
        System.out.println("아이디 체크 서비스");
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(IllegalStateException::new);
        }
    public void userJoin(UserDto userDto){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = User.builder()
                .username(userDto.getUsername())
                .password(encoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .addr(userDto.getAddr()+userDto.getAddr1()+userDto.getAddr2())
                .build();
        userRepository.save(user);
    }





    public User findUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new IllegalArgumentException("일반로그인 오류"));
    }

}
