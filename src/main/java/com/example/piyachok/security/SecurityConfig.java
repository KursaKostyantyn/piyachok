package com.example.piyachok.security;

import com.example.piyachok.constants.Role;
import com.example.piyachok.dao.UserDAO;
import com.example.piyachok.models.User;
import com.example.piyachok.controllers.filters.CustomFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDAO userDAO;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomFilter customFilter() {
        return new CustomFilter(userDAO);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http = http.csrf().disable();

        http = http.cors().configurationSource(corsConfigurationSource()).and();
        http = http.authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/refresh").permitAll()
                .antMatchers(HttpMethod.GET, "/main/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(),Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/users").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(),Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/getAuthorizedUser").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(),Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/myCabinet").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(),Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .and();
        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http = http.addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username ->
                {
                    User userByLogin = userDAO.findUserByLogin(username);
                    return new org.springframework.security.core.userdetails.User(
                            userByLogin.getLogin(),
                            userByLogin.getPassword(),
                            Collections.singletonList(new SimpleGrantedAuthority(userByLogin.getRole().name()))
                    );
                }
        );

    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
