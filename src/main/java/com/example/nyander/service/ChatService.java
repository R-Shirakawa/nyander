package com.example.nyander.service;

import com.example.nyander.controller.form.ChatForm;
import com.example.nyander.controller.form.ChatGroupForm;
import com.example.nyander.repository.ChatGroupRepository;
import com.example.nyander.repository.ChatRepository;
import com.example.nyander.repository.UserRepository;
import com.example.nyander.repository.entity.Chat;
import com.example.nyander.repository.entity.ChatGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    ChatGroupRepository chatGroupRepository;
    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    public ChatGroup saveChatGroup(int petId, int recruitmentId, int applyId) {

        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setPetId(petId);
        chatGroup.setRecruitmentId(recruitmentId);
        chatGroup.setApplyId(applyId);
        chatGroupRepository.save(chatGroup);
        return chatGroup;
    }

    public int findChat(int PetId, int RecruitmentId, int ApplyId) {
        List<ChatGroup> results = chatGroupRepository.findByPetIdAndRecruitmentIdAndApplyId(PetId, RecruitmentId, ApplyId);
        ChatGroup chatGroup = results.get(0);
        int chatId = chatGroup.getId();
        return chatId;
    }

    public List<ChatForm> findAllChats(int id) throws IOException {
        List<Chat> results = chatRepository.findByChatGroupIdOrderByCreatedDateAsc(id);
        List<ChatForm> reports = setChatForm(results);
        return reports;
    }

    private List<ChatForm> setChatForm(List<Chat> results) throws IOException {
        List<ChatForm> tasks = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ChatForm user = new ChatForm();
            Chat result = results.get(i);
            user.setId(result.getId());
            user.setChatGroupId(result.getChatGroupId());
            user.setUserId(result.getUserId());
            user.setText(result.getText());
            user.setCreatedDate(result.getCreatedDate());
            user.setUpdatedDate(result.getUpdatedDate());
            user.setUser(userService.setUserForm(result.getUser()));
            user.setChatGroup(result.getChatGroup());
            tasks.add(user);
        }
        return tasks;
    }

    public ChatGroup findChatGroupId(int id) {
        ChatGroup chatGroup = chatGroupRepository.findById(id);
        return chatGroup;
    }

    public void saveChatText(int userId, int chatGroupId, String chatText) {
        Chat chat = new Chat();
        chat.setUserId(userId);
        chat.setChatGroupId(chatGroupId);
        chat.setText(chatText);
        chatRepository.save(chat);
    }

    public List<ChatForm> findByUserId(int loginUserId) throws IOException {
        List<Chat> chatList = new ArrayList<>();
        List<ChatForm> chatFormList = new ArrayList<>();

        chatList = chatRepository.findByUserId(loginUserId);
        chatFormList = setChatForm(chatList);
        return chatFormList;
    }

    public int findFirstChat(int chatId) {
        List<Chat> results = chatRepository.findByChatGroupIdOrderByCreatedDateDesc(chatId);
        if (results.size() == 0) {
            int id = 0;
            return id;
        }
        int id = results.get(0).getId();
        return id;
    }

    public List<ChatGroupForm> findAllChatGroup() {
        List<ChatGroup> results = chatGroupRepository.findAll();
        List<ChatGroupForm> chatGroupFormList = setChatGroupForm(results);
        return chatGroupFormList;
    }

    public List<ChatGroupForm> setChatGroupForm(List<ChatGroup> results) {
        List<ChatGroupForm> chatGroupFormList = new ArrayList<>();

        for (ChatGroup result : results) {
            ChatGroupForm chatGroupForm = new ChatGroupForm();
            chatGroupForm.setId(result.getId());
            chatGroupForm.setPetId(result.getPetId());
            chatGroupForm.setRecruitmentId(result.getRecruitmentId());
            chatGroupForm.setApplyId(result.getApplyId());
            chatGroupForm.setCreatedDate(result.getCreatedDate());
            chatGroupForm.setUpdatedDate(result.getUpdatedDate());
            chatGroupForm.setPet(result.getPet());
            //フォームへセットしたペットの画像１に画像データがあれば変換処理（byte⇒BASE64形式へ）
            if (chatGroupForm.getPet().getImage1() != null) {
                byte[] byteImage1 = chatGroupForm.getPet().getImage1();
                //BASE64形式へ変換してFormにセット
                chatGroupForm.setImage1(Base64.getEncoder().encodeToString(byteImage1));
            }
            chatGroupFormList.add(chatGroupForm);
        }
        return chatGroupFormList;
    }
    public int findPetId(int chatGroupId) {
        ChatGroup results = chatGroupRepository.findById(chatGroupId);
        int id = results.getPetId();
        return id;
    }
}
