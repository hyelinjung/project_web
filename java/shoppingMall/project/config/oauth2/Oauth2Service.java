package shoppingMall.project.config.oauth2;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Oauth2Service extends DefaultOAuth2UserService {
private final UserRepository userRepository;
    private String provider = "google";
    private String providerId;
    private String email;
    private String name;


    //구글인지 네이버인지
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
       OAuth2User oAuth2User = super.loadUser(userRequest);
        oauth2UserSave(oAuth2User);
        return oAuth2User;
    }
    private User oauth2UserSave(OAuth2User oAuth2User){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Map<String,Object> attributes = oAuth2User.getAttributes();
        System.out.println("attributes"+attributes);
        if ( oAuth2User.getAttributes().containsKey("response") ){
            System.out.println("네이버");
            provider = "naver";
            Map<String,Object> cop = (Map<String, Object>) attributes.get("response");
                    name = (String) cop.get("name");
                    email =(String) cop.get("email");
            System.out.println(email);
            System.out.println("name"+name);
        }else {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        }



        //이미 회원이 경우 OR 최초 회원인 경우
        User user= userRepository.findByUsername(name)
                .map(entity-> entity.userUpdate(provider,providerId)) // provider 어느 서비스에서 정보를 제공했는지 판단
                .orElse(User.builder()
                        .username(name)
                        .password(encoder.encode("1234"))
                        .email(email)
                        .provider(provider)
                        .build());

    return userRepository.save(user);
    }
}
