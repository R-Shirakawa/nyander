package com.example.nyander.repository;

import com.example.nyander.repository.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    public List<Favorite> findAllByOrderByIdAsc();

    @Query("SELECT f FROM Favorite f INNER JOIN f.pet p ON f.pet.id = f.pet WHERE f.userId = :userId ORDER BY f.createdDate DESC")
    public List<Favorite> findByUserId(@Param("userId")int userId);

    public void deleteByPetIdAndUserId(int petId, int userId);

    public void deleteById(int id);
}
