package shoppingMall.project.config.oauth2;

import java.util.HashMap;
import java.util.Map;

public class NaverCustom implements CustomOauth2{

    private Map<String,Object> attributes;

    public NaverCustom(Map<String,Object> attributes) {
     this.attributes = attributes;
    }

    @Override
    public String username() {
        return (String) attributes.get("name");
    }

    @Override
    public String email() {
        return (String) attributes.get("email");
    }

    @Override
    public String provider() {
        return "naver";
    }

    @Override
    public String providerId() {
        return (String) attributes.get("id");
    }

    @Override
    public Map<String, Object> convertToMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("provider","naver");
        map.put("providerId", attributes.get("id"));
        map.put("name", attributes.get("name"));
        map.put("email", attributes.get("email"));
        return map;
    }
}
