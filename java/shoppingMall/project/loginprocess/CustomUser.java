/*
package shoppingMall.project.loginprocess;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import shoppingMall.project.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
@Data
public class CustomUser implements UserDetails, OAuth2User {

    private User user;
    private  Map<String, Object> getAttributes;
    public CustomUser(User user){
        this.user = user;
    }
    public CustomUser(User user, Map<String, Object> getAttributes){
        this.user = user;
        this.getAttributes = getAttributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return getAttributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(user.getRole());
            }
        });
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
*/
