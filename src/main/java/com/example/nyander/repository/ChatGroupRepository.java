package com.example.nyander.repository;

import com.example.nyander.repository.entity.ChatGroup;
import com.example.nyander.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {
    List<ChatGroup> findByPetIdAndRecruitmentIdAndApplyId(int PetId,int RecruitmentId,int ApplyId);
    public ChatGroup save(ChatGroup chatGroup);
    public ChatGroup findById(int id);


}
