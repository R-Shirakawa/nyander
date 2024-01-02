package com.example.nyander.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "post_number")
    private String postNumber;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address3")
    private String address3;

    @Column(name = "icon")
    private byte[] icon;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", insertable = false)
    private Date updatedDate;

    //登録日付自動登録処理（INSERT時に動作）
    @PrePersist
    public void onPrePersist() {
        setCreatedDate(new Date());
        setUpdatedDate(new Date());
    }
    //更新日付自動登録処理（UPDATE時に動作）
    @PreUpdate
    public void onPreUpdate(){
        setUpdatedDate(new Date());
    }
}
