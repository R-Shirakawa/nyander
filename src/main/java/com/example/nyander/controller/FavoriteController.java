package com.example.nyander.controller;

import com.example.nyander.controller.form.FavoriteForm;

import com.example.nyander.repository.entity.Pet;
import com.example.nyander.service.CategoryService;
import com.example.nyander.service.FavoriteService;
import com.example.nyander.service.PrefectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Controller
public class FavoriteController {

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PrefectureService prefectureService;

    //いいね一覧画面表示
    @GetMapping("/favorite-list")
    public ModelAndView FavoriteList() {
        ModelAndView mav = new ModelAndView();

        //ログインユーザーのIDを取得
        int userId = getLoginUser().getId();

        //いいね一覧取得
        List<FavoriteForm> favoritesList = favoriteService.findFavorites(userId);

        //カテゴリ一覧取得
        Map<Integer, String> categories = categoryService.getCategoryAll();
        //都道府県一覧取得
        Map<Integer, String> prefectures = prefectureService.getPrefectureAll();

        mav.setViewName("/favorite-list");
        mav.addObject("favoritesList", favoritesList);
        mav.addObject("categories", categories);
        mav.addObject("prefectures", prefectures);
        return mav;
    }

    //いいね登録機能
    @PostMapping("/add-favorite")
    public ModelAndView addFavorite(@RequestParam("petId") int petId) {
        ModelAndView mav = new ModelAndView();
        FavoriteForm favoriteForm = new FavoriteForm();

        Pet pet = new Pet();
        pet.setId(petId);
        favoriteForm.setPet(pet);
        favoriteService.save(favoriteForm);
        mav.setViewName("redirect:/");
        return mav;
    }

    //お気に入りリストからの削除
    @PostMapping("/favoriteList/delete/{petId}")
    public ModelAndView deleteFavoriteList(@PathVariable int petId) {
        int userId = getLoginUser().getId();
        favoriteService.deleteFavoriteList(petId, userId);
        return new ModelAndView("redirect:/favorite-list");
    }

    //いいねの取消
    @PostMapping("/favorite/delete/{petId}")
    public ModelAndView deleteFavorite(@PathVariable int petId) {
        int userId = getLoginUser().getId();
        favoriteService.deleteFavoriteList(petId, userId);
        return new ModelAndView("redirect:/");
    }
}
