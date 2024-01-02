package com.example.nyander.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class UserForm {

    private int id;

    private String name;

    private String password;

    private String confirmPassword;

    private String postNumber;

    private String address1;

    private String address2;

    private String address3;

    private String icon;
    private MultipartFile image;

    private Date createdDate;

    private Date updatedDate;
}
