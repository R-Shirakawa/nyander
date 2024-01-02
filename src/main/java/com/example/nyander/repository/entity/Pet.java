package com.example.nyander.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pets")
@Getter
@Setter
public class Pet {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "age")
    private int age;

    @Column(name = "age_month")
    private int ageMonth;

    @Column(name = "cat_type")
    private int catType;

    @Column(name = "gender")
    private int gender;

    @Column(name = "name")
    private String name;

    @Column(name = "image1")
    private byte[] image1;

    @Column(name = "image2")
    private byte[] image2;

    @Column(name = "image3")
    private byte[] image3;

    @Column(name = "image4")
    private byte[] image4;

    @Column(name = "movie")
    private byte[] movie;

    @Column(name = "color")
    private String color;

    @Column(name = "recruitment_id")
    private int recruitmentId;

    @Column(name = "prefecture_name")
    private int prefectureName;

    @Column(name = "memo")
    private String memo;

    @Column(name = "status")
    private int status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", insertable = false)
    private Date updatedDate;

//    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
//    private List<Favorite> favorites;

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
