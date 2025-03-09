package com.achiveme.mvp.config;

import com.achiveme.mvp.filter.JwtFilter;
import com.achiveme.mvp.repository.UserRepository;
import com.achiveme.mvp.service.user_details.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    private final JwtFilter jwtFilter;

    public SecurityConfig(MyUserDetailsService myUserDetailsService, UserRepository userRepository, JwtFilter jwtFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()//TODO you can get only yourself unless you're admin. Add also more info in this endpoint
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/users/{id}/change-password").authenticated()

                        .requestMatchers(HttpMethod.GET, "/challenges").permitAll()
                        .requestMatchers(HttpMethod.POST, "/challenges").authenticated()
                        .requestMatchers(HttpMethod.GET, "/challenges/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/challenges/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/challenges/{id}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/challenges/{challengeId}/participants").authenticated()
                        .requestMatchers(HttpMethod.GET, "/challenges/{challengeId}/participants").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/challenges/{challengeId}/participants/participantId").hasAnyRole("MODERATOR", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/challenges/{challengeId}/participants/participantId").hasAnyRole("MODERATOR", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/challenges/{challengeId}check-posts").authenticated()
                        .requestMatchers(HttpMethod.GET, "/challenges/{challengeId}/check-posts").authenticated()
                        .requestMatchers(HttpMethod.GET, "/challenges/check-posts/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/challenges/check-posts/{id}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/challenges/check-posts/{checkPostId}/confirm").hasAnyRole("MODERATOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/challenges/check-posts/{checkPostId}/unconfirm").hasAnyRole("MODERATOR", "ADMIN")

                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(myUserDetailsService);
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

