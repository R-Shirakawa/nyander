package com.example.nyander.controller.form;
import com.example.nyander.repository.entity.ChatGroup;
import com.example.nyander.repository.entity.User;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
@Getter
@Setter
public class ChatForm {

    private int id;

    private int userId;

    private int chatGroupId;

    private String text;

    private Date createdDate;

    private Date updatedDate;

    private UserForm user;


    private ChatGroup chatGroup;
}
