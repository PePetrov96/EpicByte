package com.project.EpicByte.config;

import com.project.EpicByte.repository.UserRepository;
import com.project.EpicByte.service.impl.MyUserServiceDetails;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    public static final String INDEX_URL = "/";
    public static final String USERS_LOGIN_URL = "/users/login";
    public static final String USERS_LOGOUT_URL = "/users/logout";
    public static final String USERS_REGISTER_URL = "/users/register";
    public static final String USERS_LOGIN_ERROR_URL = "/users/login-error";
    public static final String USERS_UNAUTHORIZED_URL = "/users/unauthorized";

    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    public static final String REMEMBER_ME_FIELD = "remember-me";
    private final String rememberMeKey;

    public SecurityConfiguration() {
        this.rememberMeKey = "topSecret";
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(
                        //Define which URLs are visible by which users
                        authorizeRequest -> authorizeRequest
                                // All static resources (js, images, css) are visible to everyone
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                // Index page is available to everyone
                                .requestMatchers(INDEX_URL).permitAll()
                                // Login page, Register page, Logout page are available to everyone
                                .requestMatchers(USERS_LOGIN_URL, USERS_LOGOUT_URL, USERS_REGISTER_URL).permitAll()
                                // Login error page, Unauthorized page are available to everyone
                                .requestMatchers(USERS_LOGIN_ERROR_URL, USERS_UNAUTHORIZED_URL).permitAll()
                                // All other requests are authenticated.
                                .anyRequest().permitAll()
                // LOGIN logic
                ).formLogin(
                        formLogin -> {
                            formLogin
                                    // Redirect to user login page when access is requested
                                    .loginPage(USERS_LOGIN_URL)
                                    // names of the input fields in the login form
                                    .usernameParameter(USERNAME_FIELD)
                                    .passwordParameter(PASSWORD_FIELD)
                                    // Redirect to successful and un-successful logins
                                    .defaultSuccessUrl(INDEX_URL)
                                    .failureForwardUrl(USERS_LOGIN_ERROR_URL);
                        }
                // LOGOUT logic
                ).logout(
                        logout -> {
                            logout
                                    // logout URL
                                    .logoutUrl(USERS_LOGOUT_URL)
                                    // Redirect after logout
                                    .logoutSuccessUrl(INDEX_URL)
                                    // Invalidate the user from the session
                                    .invalidateHttpSession(true);
                        }
                // REMEMBER ME logic (with cookie)
                ).rememberMe(
                        rememberMe ->
                                rememberMe
                                        .key(rememberMeKey)
                                        // specify the parameter name that will receive
                                        // the value representing the remember-me login request
                                        .rememberMeParameter(REMEMBER_ME_FIELD)
                                        .rememberMeCookieName(REMEMBER_ME_FIELD)
                                        // define the validity duration of the cookie that
                                        // will be used for the remember-me functionality
                                        .tokenValiditySeconds(60 * 60 * 24)
                );

//                .exceptionHandling(
//                        (exception)-> exception.accessDeniedPage(USERS_UNAUTHORIZED_URL));


        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // Translation between the project Users and Roles to representation which Spring security understands
        return new MyUserServiceDetails(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
