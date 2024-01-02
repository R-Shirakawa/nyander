package com.example.nyander.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // ログイン成功時のカスタム処理
        // Remember Me オプションのチェックを取得
        String rememberMe = request.getParameter("isRememberMe");

        if ("on".equals(rememberMe)) {
            // Remember Me オプションが選択された場合、ResponseCookieを設定
            createCookie(request.getParameter("username"), request.getParameter("password"), response);
        } else {
            // Remember Me オプションが選択されていない場合、Cookieを削除
            deleteCookie("savedUsername", response);
            deleteCookie("savedPassword", response);
        }

        response.sendRedirect("/nyander/");
    }
    private void deleteCookie(String cookieName, HttpServletResponse response) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName(cookieName);
        cookieGenerator.setCookieMaxAge(0); // Cookieを即座に無効にする
        cookieGenerator.setCookiePath("/"); // Cookieの有効範囲
        cookieGenerator.addCookie(response, null);
    }
    public static void createCookie(String userEmail, String password, HttpServletResponse response) {
        ResponseCookie savedUsernameCookie = ResponseCookie.from("savedUsername", userEmail)
                .maxAge(Duration.ofDays(30)) // 30日
                .path("/nyander/login") // Cookieの有効範囲
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, savedUsernameCookie.toString());

        // UserEditの時に、パスワードは更新しない場合があるため、条件分岐しておく。
        if(!password.isEmpty()) {
            ResponseCookie savedPasswordCookie = ResponseCookie.from("savedPassword", password)
                .maxAge(Duration.ofDays(30)) // 30日
                .path("/nyander/login") // Cookieの有効範囲
                .build();
            response.addHeader(HttpHeaders.SET_COOKIE, savedPasswordCookie.toString());
        }
    }
}