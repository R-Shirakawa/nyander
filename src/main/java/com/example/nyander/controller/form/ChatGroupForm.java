package com.example.nyander.controller.form;
import com.example.nyander.repository.entity.Pet;
import com.example.nyander.repository.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ChatGroupForm {

    private int id;

    private int petId;

    private int recruitmentId;

    private int applyId;

    private Date createdDate;

    private Date updatedDate;

    private Pet pet;
    private String Image1;
}
