package com.example.buchladen.Configuration;

import com.example.buchladen.Service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


   private final CustomUserDetailsService customUserDetailsService;

   private final AfterLoginForAdminAndUser afterLoginForAdminAndUser;

   private final CustomLoginFailureHandler customLoginFailureHandler;





    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          AfterLoginForAdminAndUser afterLoginForAdminAndUser,
                          CustomLoginFailureHandler customLoginFailureHandler) {
        this.customUserDetailsService = customUserDetailsService;

        this.afterLoginForAdminAndUser = afterLoginForAdminAndUser;
        this.customLoginFailureHandler = customLoginFailureHandler;

    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .userDetailsService(customUserDetailsService)

                .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/home", "/uploads/**", "/login", "/css/**", "/images/**", "/user_form", "/js/**", "/books/**", "/search").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/my_account/**", "/forgot_password", "/reset_password", "/payment", "/cart/**", "/order").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureHandler(customLoginFailureHandler)
                        .successHandler(afterLoginForAdminAndUser)
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )


                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/home?logout")
                        .permitAll()
                )
                .userDetailsService(customUserDetailsService)
                .rememberMe(rememberMe -> rememberMe
                        .alwaysRemember(true)
                        .rememberMeParameter("remember-me")
                        .rememberMeCookieName("javasampleapproach-remember-me")
                        .tokenValiditySeconds(24 * 60 * 60)
                        .userDetailsService(customUserDetailsService)
                )

        ;

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}
