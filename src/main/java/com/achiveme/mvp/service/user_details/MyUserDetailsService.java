package com.achiveme.mvp.service.user_details;

import com.achiveme.mvp.entity.User;
import com.achiveme.mvp.entity.UserPrincipal;
import com.achiveme.mvp.exception.User.EmailDoesNotExistException;
import com.achiveme.mvp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailDoesNotExistException("User with email: " + email + " not found"));
        return new UserPrincipal(user);
    }
}
