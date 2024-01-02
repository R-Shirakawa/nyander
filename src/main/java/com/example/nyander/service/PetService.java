package com.example.nyander.service;

import com.example.nyander.controller.form.PetForm;
import com.example.nyander.repository.PetRepository;
import com.example.nyander.repository.entity.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import com.example.nyander.repository.CategoryRepository;
import com.example.nyander.repository.PrefectureRepository;
import com.example.nyander.repository.entity.Category;
import com.example.nyander.repository.entity.Prefecture;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import static com.example.nyander.security.SecurityConfig.getLoginUser;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PrefectureRepository prefectureRepository;

    public List<PetForm> findById(int petId) {
        List<Pet> pets = petRepository.findById(petId);
        if (pets != null) {
            return setPetForm(pets);
        } else {
            return null;
        }
    }

    private List<PetForm> setPetForm(List<Pet> pets) {
        List<PetForm> petsForm = new ArrayList<>();
        for (Pet pet : pets) {
            PetForm petForm = new PetForm();
            petForm.setId(pet.getId());
            petForm.setAge(pet != null ? pet.getAge() : null);
            petForm.setAgeMonth(pet != null ? pet.getAgeMonth() : null);
            petForm.setRecruitmentId(pet.getRecruitmentId());
            petForm.setCatType(pet != null ?  pet.getCatType() : null);
            petForm.setGender(pet.getGender());
            petForm.setName(pet.getName());
            //フロントへ渡す前にBYTE形式からBASE64形式へ変換
            petForm.setImage1(pet.getImage1() != null ? Base64.getEncoder().encodeToString(pet.getImage1()) : null);
            petForm.setImage2(pet.getImage2() != null ? Base64.getEncoder().encodeToString(pet.getImage2()) : null);
            petForm.setImage3(pet.getImage3() != null ? Base64.getEncoder().encodeToString(pet.getImage3()) : null);
            petForm.setImage4(pet.getImage4() != null ? Base64.getEncoder().encodeToString(pet.getImage4()) : null);
            petForm.setMovie(pet.getMovie() != null ? Base64.getEncoder().encodeToString(pet.getMovie()) : null);
            petForm.setColor(pet != null ? pet.getColor() : null);
            petForm.setPrefectureName(pet.getPrefectureName());
            petForm.setMemo(pet != null ? pet.getMemo() : null);
            petForm.setStatus(pet.getStatus());
            petForm.setCreatedDate(pet.getCreatedDate());
            petForm.setUpdatedDate(pet.getUpdatedDate());

            petsForm.add(petForm);
        }
        return petsForm;
    }

    public List<PetForm> findFavoriteList() {
        List<Pet> results = new ArrayList<>();
        results = petRepository.findAllByOrderByStatusAsc();
        List<PetForm> petList = setPetForm(results);
        return petList;
    }

    public List<PetForm> findAllPetList(Integer category, Integer prefecture, String searchWord) {
        List<Pet> results = new ArrayList<>();

        //全件取得
        if(category == 0 && prefecture == 0 && (searchWord == null || searchWord.isEmpty())){
            results = petRepository.findAllByOrderByStatusAsc();
        } else if (category != 0 && prefecture == 0 && searchWord.isEmpty()) {  //カテゴリ絞込
            results = petRepository.findByCatTypeOrderByStatusAsc(category);
        } else if (category == 0 && prefecture == 0 && StringUtils.hasText(searchWord)) {  //検索ワード絞込
            results = petRepository.findByNameContainingOrderByStatusAsc(searchWord);
        } else if (category == 0 && prefecture != 0 && searchWord.isEmpty()) { //お住まいの地域絞込
            results = petRepository.findByPrefectureNameOrderByStatusAsc(prefecture);
        } else if (prefecture == 0) { //カテゴリと検索ワードでの絞込
            results = petRepository.findByCatTypeAndNameContainingOrderByStatusAsc(category, searchWord);
        }else if (category == 0) { //お住まいの地域と検索ワードでの絞込
            results = petRepository.findByPrefectureNameAndNameContainingOrderByStatusAsc(prefecture, searchWord);
        } else if (searchWord.isEmpty()) { //カテゴリーとお住まいの地域での絞込
            results = petRepository.findByCatTypeAndPrefectureNameOrderByStatusAsc(category, prefecture);
        } else if (category != 0 && prefecture != 0 && StringUtils.hasText(searchWord)) { //全てで絞込
            results = petRepository.findByCatTypeAndPrefectureNameAndNameContainingOrderByStatusAsc(category, prefecture, searchWord);
        }
        List<PetForm> petList = setPetForm(results);
        return petList;
    }

    public Map<Integer, String> findAllCategory() {
        List<Category> results = categoryRepository.findAll();
        HashMap<Integer, String> category = new HashMap<>();
        for (Category result : results) {
            category.put(result.getId(), result.getCategory());
        }
        return category;
    }

    public Map<Integer, String> findAllPrefectureName() {
        List<Prefecture> results = prefectureRepository.findAll();
        HashMap<Integer, String> prefecture = new HashMap<>();
        for (Prefecture result : results) {
            prefecture.put(result.getId(), result.getPrefectureName());
        }
        return prefecture;
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Pet setPet(PetForm reqPet) throws IOException {
        Pet pet = new Pet();

        pet.setId(reqPet.getId());
        pet.setAge(reqPet.getAge());
        pet.setAgeMonth(reqPet.getAgeMonth());
        pet.setRecruitmentId(getLoginUser().getId());
        pet.setCatType(reqPet.getCatType());
        pet.setGender(reqPet.getGender());
        pet.setName(reqPet.getName());
        pet.setColor(reqPet.getColor());
        pet.setPrefectureName(reqPet.getPrefectureName());
        pet.setMemo(reqPet.getMemo());
        pet.setStatus(reqPet.getStatus());
        pet.setCreatedDate(reqPet.getCreatedDate());
        pet.setUpdatedDate(reqPet.getUpdatedDate());
        if(reqPet.getCatImage1() != null && !reqPet.getCatImage1().isEmpty()) {
            //DB保存前にMultipartFile型　⇒　BYTE変換してImageとMovieをDBへセット
            pet.setImage1(reqPet.getCatImage1().getBytes());
        }
        if(reqPet.getCatImage2() != null && !reqPet.getCatImage2().isEmpty()) {
            pet.setImage2(reqPet.getCatImage2().getBytes());
        }
        if (reqPet.getCatImage3() != null && !reqPet.getCatImage3().isEmpty()) {
            pet.setImage3(reqPet.getCatImage3().getBytes());
        }
        if (reqPet.getCatImage4() != null && !reqPet.getCatImage4().isEmpty()) {
            pet.setImage4(reqPet.getCatImage4().getBytes());
        }
        if (reqPet.getCatMovie() != null && !reqPet.getCatMovie().isEmpty()) {
            pet.setMovie(reqPet.getCatMovie().getBytes());
        }
        return pet;
    }

    //編集時に新規ファイルがなかった場合既存のファイルを再セット
    public Pet updatePet(Pet pet){

        Pet existPet = petRepository.findById(pet.getId()).get(0);

        if(pet.getImage1() == null) {
            //DB保存前にMultipartFile型　⇒　BYTE変換してImageとMovieをDBへセット
            pet.setImage1(existPet.getImage1());
        }
        if(pet.getImage2() == null) {
            pet.setImage2(existPet.getImage2());
        }
        if (pet.getImage3() == null) {
            pet.setImage3(existPet.getImage3());
        }
        if (pet.getImage4() == null) {
            pet.setImage4(existPet.getImage4());
        }
        if (pet.getMovie() == null) {
            pet.setMovie(existPet.getMovie());
        }
        return pet;
    }

    //編集するペットの呼び出し
    public PetForm editPet(Integer id) {
        List<Pet> results = new ArrayList<>();
        results.add((Pet) petRepository.findById(id).orElse(null));
        List<PetForm> pets = setPetForm(results);
        return pets.get(0);
    }

    //ペット登録
    public void savePet(Pet savePet) {
        petRepository.save(savePet);
    }

    // 画像、動画のファイル化(BYTE → Base64)
    public static String convertByte(byte[] results) throws IOException {
        return Base64.getEncoder().encodeToString(results);
    }

    // ファイルのbyte[]化(Base64 → BYTE)
    public byte[] convertFile(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    //base64のbyte[]化
    public byte[] convertBase64(String base64){
        return Base64.getDecoder().decode(base64);
    }
}