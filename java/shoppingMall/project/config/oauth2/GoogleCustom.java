package shoppingMall.project.config.oauth2;

import java.util.HashMap;
import java.util.Map;

public class GoogleCustom implements CustomOauth2{

    private Map<String,Object> attributes;

    public GoogleCustom(Map<String,Object> attributes) {
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
        return "google";
    }

    @Override
    public String providerId() {
        return (String) attributes.get("sub");
    }

    @Override
    public Map<String, Object> convertToMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("provider","google");
        map.put("providerId", attributes.get("id"));
        map.put("name", attributes.get("name"));
        map.put("email", attributes.get("email"));
        return map;
    }
}
