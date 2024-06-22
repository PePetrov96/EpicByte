package com.project.EpicByte.config;

import com.project.EpicByte.model.entity.enums.UserRolesEnum;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static com.project.EpicByte.util.Constants.*;

@Configuration
public class SecurityConfiguration {
    private final String rememberMeKey;

    public SecurityConfiguration() {
        this.rememberMeKey = "topSecret";
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/admin/**"))
                .authorizeHttpRequests(
                        //Define which URLs are visible by which users
                        authorizeRequest -> authorizeRequest
                                // All static resources (js, images, css) are visible to everyone
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                // Index page is available to everyone
                                .requestMatchers(INDEX_URL).permitAll()
                                .requestMatchers("/moderator/**").hasAnyRole(UserRolesEnum.MODERATOR.name(), UserRolesEnum.ADMIN.name())
                                // All /admin links ara available to MODERATOR and ADMIN
                                .requestMatchers("/admin/**").hasRole(UserRolesEnum.ADMIN.name())
                                // All API /admin links ara available to ADMIN
                                .requestMatchers("/api/admin/**").permitAll()
                                // All API /user links ara available to everyone
                                .requestMatchers("/api/user/**").permitAll()
                                // User cart page, user cart checkout confirmation page, user orders list  page are available to logged-in users only
                                .requestMatchers(USER_CART_URL, USERS_CART_CHECKOUT_CONFIRM_URL, USER_ORDERS_URL).authenticated()
                                // All /moderator links ara available to MODERATOR and ADMIN
                                // test
                                .requestMatchers(PRODUCT_ADD_TO_CART_URL).authenticated()
                                // Not-logged in users can access Register and Login pages
                                .requestMatchers(LOGIN_URL, REGISTER_URL).anonymous()
                                // Logged-in users can access logout page
                                .requestMatchers(LOGOUT_URL).authenticated()
                                // Login error page, Unauthorized page are available to everyone
                                .requestMatchers(LOGIN_ERROR_URL, USER_UNAUTHORIZED_URL).permitAll()
                                // Books, Textbooks, Music, Movies, Toys pages are available to everyone
                                .requestMatchers(ALL_BOOKS_URL, ALL_TEXTBOOKS_URL, ALL_MUSIC_URL, ALL_MOVIES_URL, ALL_TOYS_URL).permitAll()
                                // Terms and Conditions page, Privacy page ara available to everyone
                                .requestMatchers(TERMS_AND_CONDITIONS_URL, PRIVACY_URL).permitAll()
                                // All other requests are permitted
                                .anyRequest().permitAll()
                        // LOGIN logic
                ).formLogin(
                        formLogin -> {
                            formLogin
                                    // Redirect to user login page when access is requested
                                    .loginPage(LOGIN_URL)
                                    // names of the input fields in the login form
                                    .usernameParameter(USERNAME_FIELD)
                                    .passwordParameter(PASSWORD_FIELD)
                                    // Redirect to successful and un-successful logins
                                    .defaultSuccessUrl(INDEX_URL, true)
                                    .failureForwardUrl(LOGIN_ERROR_URL);
                        }
                        // LOGOUT logic
                ).logout(
                        logout -> {
                            logout
                                    // logout URL
                                    .logoutUrl(LOGOUT_URL)
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


        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        // Translation between the project Users and Roles to representation which Spring security understands
        return new MyUserServiceDetails(userRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}
