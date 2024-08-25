package shoppingMall.project.config.oauth2;

import lombok.Data;

import java.util.Map;


public interface CustomOauth2 {
    public String username();
    public String email();
    public String provider();
    public String providerId();
    public Map<String,Object> convertToMap();
}
