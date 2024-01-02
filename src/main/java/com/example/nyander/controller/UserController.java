package com.example.nyander.controller;

import com.example.nyander.controller.form.UserForm;
//import com.example.nyander.service.UserService;
import com.example.nyander.repository.entity.User;
import com.example.nyander.service.PrefectureService;
import com.example.nyander.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

import static com.example.nyander.security.CustomAuthenticationSuccessHandler.createCookie;
import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Controller
public class UserController {
    @Autowired
    HttpSession session;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PrefectureService prefectureService;

    @GetMapping("/login")
    public ModelAndView login(HttpServletRequest request) {

        ModelAndView mav = new ModelAndView();

        // ユーザー名とパスワードをCookieから取得
        String savedUsername = null;
        String savedPassword = null;
        int cookieSaveCount = 0;
        for (Cookie cookie : request.getCookies()) {
            if ("savedUsername".equals(cookie.getName())) {
                savedUsername = cookie.getValue();
                cookieSaveCount++;
            } else if ("savedPassword".equals(cookie.getName())) {
                savedPassword = cookie.getValue();
                cookieSaveCount++;
            }
        }
        // ユーザー名とパスワードをModelAndViewに追加
        mav.addObject("savedUsername", savedUsername);
        mav.addObject("savedPassword", savedPassword);
        mav.addObject("isRememberMe", cookieSaveCount == 2);

        UserForm userForm = new UserForm();
        mav.addObject("signUpForm", userForm);
        mav.setViewName("/login");
        return mav;
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        ModelAndView mav = new ModelAndView();
        UserForm userForm = new UserForm();
        mav.addObject("signUpForm", userForm);
        mav.addObject("prefectures", prefectureService.getPrefectureAll());
        mav.setViewName("/signup");
        return mav;
    }

    @PostMapping("/signup")
    public ModelAndView signup(@ModelAttribute("signUp-form") UserForm userForm) throws IOException {
        userService.save(userForm);
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/duplicationCheck")
    @ResponseBody
    public String duplicationCheck(@RequestParam String name) {
        String result;
        if (userService.existsByName(name)) {
            if (getLoginUser() != null && userService.findByName(name).getId() == getLoginUser().getId()) {
                // ログイン中 かつ 入力したアカウント名が自分のアカウント名
                result = "false";
            } else {
                result = "true";
            }
        } else {
            result = "false";
        }
        return result;
    }

    @GetMapping("/editUser")
    public ModelAndView editUser() throws IOException {
        ModelAndView mav = new ModelAndView();
        UserForm userForm = userService.setUserForm(getLoginUser());
        mav.addObject("userForm", userForm);
        mav.addObject("prefectures", prefectureService.getPrefectureAll());
        mav.setViewName("/editUser");
        return mav;
    }

    @PostMapping("/editUser")
    public ModelAndView editUser(HttpServletResponse response, @ModelAttribute("editUserModel")@Validated UserForm userForm, BindingResult result) throws IOException {

        String password = (userForm.getPassword() != null ? userForm.getPassword() : "");
        userService.update(userForm);
        createCookie(userForm.getName(), password, response);
        return new ModelAndView("redirect:/");
    }

}
