package com.example.nyander.security;

import com.example.nyander.repository.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //アクセス制御設定
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedPage("/?noPermission")
        ).authorizeHttpRequests(authz -> authz.
                mvcMatchers("/css/**").permitAll().
                mvcMatchers("/js/**").permitAll().
                mvcMatchers("/image/**").permitAll().
                mvcMatchers("/signup").permitAll().
                mvcMatchers("/").permitAll().
                mvcMatchers("/header").permitAll().
                mvcMatchers("/petDetail").permitAll().
                mvcMatchers("/duplicationCheck").permitAll().
                anyRequest().authenticated()
        ).formLogin(login -> login
                .loginPage("/login?unauthorized")
                .loginProcessingUrl("/login").
                /*defaultSuccessUrl("/").*/
                usernameParameter("username").
                usernameParameter("username").
                passwordParameter("password").
                successHandler(customAuthenticationSuccessHandler()).
                failureUrl("/login?error").
                permitAll()
        ).logout(logout -> logout.
                logoutSuccessUrl("/login?logout").permitAll().
                invalidateHttpSession(true)
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    public static User getLoginUser() {
        User loginUser = null;
        // 現在の認証情報を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 認証情報がUserDetailsImplのインスタンスがどうか確認
        if (authentication.getPrincipal() instanceof AccountUserDetails) {
            // UserDetailsImplにキャストして変数に格納
            AccountUserDetails userDetailsImpl = (AccountUserDetails) authentication.getPrincipal();
            // UserDetailsImplからログインユーザーを取得（これは自分でメソッドを追加する必要あり）
            loginUser = userDetailsImpl.getUser();
        }
        return loginUser;
    }

    public void setLoginUser(User user) {
        AccountUserDetails newPrincipal = new AccountUserDetails(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(newPrincipal, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }
}