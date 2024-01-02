package com.example.nyander.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "prefectures")
@Getter
@Setter
public class Prefecture {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "prefecture_name")
    private String prefectureName;

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
