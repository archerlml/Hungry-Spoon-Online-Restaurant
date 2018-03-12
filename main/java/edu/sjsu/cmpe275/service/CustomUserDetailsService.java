package edu.sjsu.cmpe275.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import edu.sjsu.cmpe275.domain.User;
import edu.sjsu.cmpe275.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("not found");
        }
        if(user.getRole().equals("ROLE_waitforverify")){
            throw new DisabledException("Please check your email to confirm your registration");
        }
        
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        System.out.println("username is " + username + ", " + user.getRole());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }
}