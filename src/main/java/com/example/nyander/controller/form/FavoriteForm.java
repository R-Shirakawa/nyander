package com.example.nyander.controller.form;

import com.example.nyander.repository.entity.Pet;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FavoriteForm {

    private int id;

    private int userId;

    private Pet pet;
    private String Image1;

    private Date createdDate;

    private Date updatedDate;
}
