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
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080"));
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
                .antMatchers(HttpMethod.GET, "/main/**").permitAll()
                .antMatchers(HttpMethod.GET, "/getAuthorizedUser").permitAll()
                .antMatchers(HttpMethod.POST, "/refresh").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/comments/placeComments").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.POST, "/comments").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/comments").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/comments").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.DELETE, "/comments/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/comments/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/features").permitAll()
                .antMatchers(HttpMethod.POST, "/features").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.PUT, "/features/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.DELETE, "/features/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.GET, "/features/**").permitAll()
                .antMatchers(HttpMethod.GET, "/news/allNews").permitAll()
                .antMatchers(HttpMethod.POST, "/news").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/news/mainNews").permitAll()
                .antMatchers(HttpMethod.GET, "/news").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/news/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.DELETE, "/news/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/news/**").permitAll()
                .antMatchers(HttpMethod.POST, "/places").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/places").permitAll()
                .antMatchers(HttpMethod.GET, "/places/myPlaces").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/places/activated").permitAll()
                .antMatchers(HttpMethod.GET, "/places/notActivated").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.PUT, "/places/addPhotos").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/places/placePhoto/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/places/sendMailToAdmin/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/places/search/findPLaceByName").permitAll()
                .antMatchers(HttpMethod.GET, "/places/filters").permitAll()
                .antMatchers(HttpMethod.GET, "/places/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/places/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/places/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/ratings").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.GET, "/ratings/myRatings").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.POST, "/ratings").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/ratings/rating").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/ratings").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/ratings/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/refreshTokens").permitAll()
                .antMatchers(HttpMethod.GET, "/search/findPLaceByTypesId").permitAll()
                .antMatchers(HttpMethod.GET, "/types").permitAll()
                .antMatchers(HttpMethod.POST, "/types").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.GET, "/types/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/types/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.DELETE, "/types/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.GET, "/users").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.PUT, "/users/update").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/users/favoritePlaces").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/users/favoritePlaces/add").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/users/favoritePlaces/check").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.DELETE, "/users/favoritePlaces/delete").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/activate").permitAll()
                .antMatchers(HttpMethod.GET, "/users/sendResetPasswordToken").permitAll()
                .antMatchers(HttpMethod.GET, "/users/resetPassword").permitAll()
                .antMatchers(HttpMethod.PUT, "/users/addPhoto").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/users/userPhoto/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/users/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.GET, "/tops").permitAll()
                .antMatchers(HttpMethod.GET, "/tops/**").permitAll()
                .antMatchers(HttpMethod.POST, "/tops").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.DELETE, "/tops/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())
                .antMatchers(HttpMethod.PUT, "/tops/addPlace").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/tops/deletePlace").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole(), Role.ROLE_ADMIN.getUserRole(), Role.ROLE_USER.getUserRole())
                .antMatchers(HttpMethod.PUT, "/tops/**").hasAnyRole(Role.ROLE_SUPERADMIN.getUserRole())

                .and();

        http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

        http = http.addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username ->
                {
                    User userByLogin = userDAO.findUserByLogin(username).orElse(new User());
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
