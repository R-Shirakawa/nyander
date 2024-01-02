package com.example.nyander.repository;

import com.example.nyander.repository.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findByChatGroupId(int Id);
    List<Chat> findByUserId(int loginUserId);
    Chat findFirstByOrderByIdDesc();
    List<Chat> findByChatGroupIdOrderByCreatedDateDesc(int chatId);
    List<Chat> findByChatGroupIdOrderByCreatedDateAsc(int chatId);
}

