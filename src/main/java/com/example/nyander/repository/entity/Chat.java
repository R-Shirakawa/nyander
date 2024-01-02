package com.example.nyander.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "chats")
@Getter
@Setter
public class Chat {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "chat_group_id")
    private int chatGroupId;

    @Column(name = "text")
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date", insertable = false)
    private Date updatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_group_id", insertable = false, updatable = false)
    private ChatGroup chatGroup;

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
    // 最新のデータを取得するメソッド
//    @Query("SELECT c FROM Chat c ORDER BY c.id DESC")
//    Chat findLatestChat();
}
