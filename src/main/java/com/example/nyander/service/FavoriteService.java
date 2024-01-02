package com.example.nyander.service;

import com.example.nyander.controller.form.FavoriteForm;
import com.example.nyander.repository.FavoriteRepository;
import com.example.nyander.repository.entity.Favorite;
import com.example.nyander.repository.entity.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Service
public class FavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;

    public List<FavoriteForm> findFavorites(int userId) {
        List<FavoriteForm> favoritesList = new ArrayList<>();
        List<Favorite> results = favoriteRepository.findByUserId(userId);
        favoritesList = setFavoriteForm(results);
        return favoritesList;
    }

    public List<Integer> favoritePetId(int userId) {
        List<Integer> favoritePetIdList = new ArrayList<>();
        List<FavoriteForm> results = findFavorites(userId);
        for (int i = 0; i < results.size(); i++){
            favoritePetIdList.add(results.get(i).getPet().getId());
        }
        return favoritePetIdList;
    }

    public void save(FavoriteForm favoriteForm) {
        Favorite favorite;
        favorite = setFavoriteEntity(favoriteForm);
        favoriteRepository.save(favorite);
    }

    public Favorite setFavoriteEntity(FavoriteForm favoriteForm) {
        Favorite favoriteEntity = new Favorite();
        favoriteEntity.setUserId(getLoginUser().getId());
        favoriteEntity.setPet(favoriteForm.getPet());
        return favoriteEntity;
    }

    private List<FavoriteForm> setFavoriteForm(List<Favorite> results) {
        List<FavoriteForm> favoriteForms = new ArrayList<>();
        for (int i = 0; i < results.size(); i++ ) {
            FavoriteForm favoriteForm = new FavoriteForm();
            Favorite result = results.get(i);
            favoriteForm.setId(result.getId());
            favoriteForm.setUserId(result.getUserId());
            favoriteForm.setPet(result.getPet());
            //フォームへセットしたペットの画像１に画像データがあれば変換処理（byte⇒BASE64形式へ）
            if(favoriteForm.getPet().getImage1() != null){
                //pet内のimage1（BYTEA型）を取り出す
                byte[] byteImage1 = favoriteForm.getPet().getImage1();
                //BASE64形式へ変換してFormにセット
                favoriteForm.setImage1(Base64.getEncoder().encodeToString(byteImage1));
            }
            favoriteForm.setCreatedDate(result.getCreatedDate());
            favoriteForm.setUpdatedDate(result.getUpdatedDate());
            favoriteForms.add(favoriteForm);
        }
        return favoriteForms;
    }

    @Transactional
    public void deleteFavoriteList(int petId, int userId) {
        favoriteRepository.deleteByPetIdAndUserId(petId, userId);
    }
}
