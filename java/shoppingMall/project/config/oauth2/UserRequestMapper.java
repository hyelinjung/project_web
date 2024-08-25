package shoppingMall.project.config.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import shoppingMall.project.domain.UserDto;

@Component //oauth2로그인 success 시 attributes 의 정보로 토큰생성을 위하여
public class UserRequestMapper {
    public UserDto dto(OAuth2User oAuth2User){
        var attributes = oAuth2User.getAttributes();
        System.out.println("attributes:"+attributes);
        UserDto dto = new UserDto();
        dto.setEmail((String) attributes.get("email"));
        dto.setUsername((String) attributes.get("name"));
        return dto;
    }
}
