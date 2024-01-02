package com.example.nyander.controller;

import com.example.nyander.controller.form.ChatForm;
import com.example.nyander.controller.form.PetForm;
import com.example.nyander.controller.form.UserForm;
import com.example.nyander.repository.entity.User;
import com.example.nyander.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Controller
public class TopController {
    @Autowired
    UserService userService;
    @Autowired
    PetService petService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PrefectureService prefectureService;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    HttpSession session;

    @Autowired
    ChatService chatService;


    @GetMapping("/")
    public ModelAndView top(@RequestParam(name="category", value = "category", defaultValue = "0") Integer category,
                            @RequestParam(name="prefecture", value = "prefecture", defaultValue = "0") Integer prefecture,
                            @RequestParam(name = "searchWord", value="searchWord", required = false) String searchWord,
                            HttpSession session) throws IOException {

        ModelAndView mav = new ModelAndView();
        List<PetForm> pets = petService.findAllPetList(category, prefecture, searchWord);
        mav.addObject("pets", pets);

        User loginUser = getLoginUser();

        List<Integer> favoritesList = new ArrayList<>();

        if(loginUser != null){
            favoritesList = favoriteService.favoritePetId(loginUser.getId());
            int loginId = loginUser.getId();
            mav.addObject("loginId", loginId);
            // headerの表示切替に使用
            UserForm loginUserForm = userService.setUserForm(loginUser);
            session.setAttribute("loginUser", loginUserForm);
        } else {
            // headerの表示切替に使用
            session.setAttribute("loginUser", loginUser);
        }

        mav.addObject("favoritesList", favoritesList);

        Map<Integer, String> categories = categoryService.getCategoryAll();
        Map<Integer, String> prefectures = prefectureService.getPrefectureAll();

        mav.setViewName("/top");

        //カテゴリの検索値保持
        mav.addObject("selectedCategory", category);
        //都道府県の検索値保持
        mav.addObject("selectedPrefecture", prefecture);
        //カテゴリの検索ワード保持
        mav.addObject("loginUser", loginUser);
        mav.addObject("searchWord", searchWord);
        mav.addObject("categories", categories);
        mav.addObject("prefectures", prefectures);
        return mav;
    }
}
