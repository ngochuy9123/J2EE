package com.springboot.j2ee.config;

import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndOtpConfirm(username,true);
        if(user==null){
            throw new UsernameNotFoundException("User Not Found");
        }
        else{
            return new CustomUser(user);
        }
    }
}
