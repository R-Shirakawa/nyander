package com.example.nyander.repository;

import com.example.nyander.repository.entity.Prefecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrefectureRepository extends JpaRepository<Prefecture, Integer> {

    List<Prefecture> findAllByOrderByIdAsc();
}