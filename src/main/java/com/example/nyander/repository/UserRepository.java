package com.example.nyander.repository;

import com.example.nyander.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByName(String name);
    public boolean existsByName(String name);
    public User findById(int recruitmentId);
    @Modifying  // nativeQueryで返り値がvoidのとき、このアノテーションが必要
    @Transactional
    @Query(value = "UPDATE users SET name = ?1, post_number = ?2, address1 = ?3, address2 = ?4, address3 = ?5, icon = ?6, updated_date = ?7 WHERE id = ?8", nativeQuery = true)
    public void updateWithoutPassword(String name, String postNumber, String address1, String address2, String address3, byte[] icon, Date updatedDate, int id);
}
