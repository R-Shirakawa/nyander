package com.example.nyander.repository;

import com.example.nyander.repository.entity.Pet;
import com.example.nyander.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PetRepository extends JpaRepository<Pet, Integer> {

    //全件取得
    public List<Pet> findAllByOrderByStatusAsc();

    //カテゴリ絞込
    public List<Pet> findByCatTypeOrderByStatusAsc(Integer Category);

    //検索ワード絞込
    public List<Pet> findByNameContainingOrderByStatusAsc(String searchWord);

    //お住まいの地域絞込
    public List<Pet> findByPrefectureNameOrderByStatusAsc(Integer Prefectures);

    //カテゴリと検索ワードでの絞込
    public List<Pet> findByCatTypeAndNameContainingOrderByStatusAsc(Integer category, String searchWord);

    //お住まいの地域と検索ワードでの絞込
    public List<Pet> findByPrefectureNameAndNameContainingOrderByStatusAsc(Integer prefecture, String searchWord);

    //お住まいの地域と検索ワードでの絞込
    public List<Pet> findByCatTypeAndPrefectureNameOrderByStatusAsc(Integer category, Integer prefecture);

    //全てで絞込
    public List<Pet> findByCatTypeAndPrefectureNameAndNameContainingOrderByStatusAsc(Integer category, Integer prefecture, String searchWord);

    //編集対象のレコード検索
    public List<Pet> findById(int id);
}

