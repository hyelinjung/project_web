package shoppingMall.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shoppingMall.project.entity.User;
import shoppingMall.project.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class loginService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return userRepository.findByUsername(username)
               .orElseThrow(()->new IllegalArgumentException("아이디 잘 못됨"));

    }
}
