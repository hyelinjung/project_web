package shoppingMall.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shoppingMall.project.constant.Role;
import shoppingMall.project.domain.UserDto;


import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;
    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String email;
    private String addr;
    @Enumerated(EnumType.STRING)
    private Role role; //user, admin
    private String provider; //oauth2
    private String providerId;

   /* public static User createUser(UserDto userDto, String provider, String providerId){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(Role.ROLE_USER);
        user.setUsername(userDto.getUsername());
        user.setProvider(provider);
        user.setProviderId(providerId);
        String add = userDto.getAddr()+userDto.getAddr1()+userDto.getAddr2();
        user.setAddr(add);
        return user;
    }*/
    @Builder
    public User(String username,String password,String email,String addr,Role role,String provider,String providerId){
        this.username = username;
        this.password = password;
        this.email = email;
        this.addr = addr;
        this.role = Role.ROLE_USER;
        this.provider =provider;
        this.providerId = providerId;
    }
    //oauth2 회원가입 시 기존회원 업데이트
    public User userUpdate(String provider,String providerId){
        this.provider =provider;
        this.providerId = providerId;
        return this;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(String.valueOf(this.role)));
    }

    @Override
    public String getUsername() {
        return username;
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

}
