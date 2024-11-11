package com.code.duan1.config;

import com.code.duan1.entity.Admin;
import com.code.duan1.entity.User;
import com.code.duan1.repository.AdminRepository;
import com.code.duan1.repository.UserRepository;
import com.code.duan1.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
public class ApplicationConfig {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    @Bean
    public CookieUtil cookieUtil(){
        return new CookieUtil();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Optional<User> user= userRepository.findUserByUsername(username);
            if(user.isPresent()){
                return user.get();
            }else{
                Optional<Admin> manager= adminRepository.findAdminByUsername(username);
                if(manager.isPresent()){
                    return manager.get();
                }else{
                    throw new UsernameNotFoundException("userName is not found");
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
