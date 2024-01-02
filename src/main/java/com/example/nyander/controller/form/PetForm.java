package com.example.nyander.controller.form;

import com.example.nyander.validation.ValidImageFile;
import com.example.nyander.validation.ValidVideoFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Getter
@Setter
public class PetForm {

    private int id;

    private int age;

    private int ageMonth;

    @Positive(groups = PositiveValidGroup1.class, message = "猫の種類を選択してください")
    private int catType;

    @Positive(groups = PositiveValidGroup2.class, message = "猫の性別を選択してください")
    private int gender;

    @NotBlank(groups = PetNameValidGroup1.class, message = "名前を入力してください")
    private String name;

    private String image1;

    @ValidImageFile(groups = ImgValidGroup.class, message = "無効なファイル形式です。画像ファイルを選択してください")
    public MultipartFile catImage1;

    private String image2;

    @ValidImageFile(groups = ImgValidGroup.class, message = "無効なファイル形式です。画像ファイルを選択してください")
    public MultipartFile catImage2;

    private String image3;

    @ValidImageFile(groups = ImgValidGroup.class, message = "無効なファイル形式です。画像ファイルを選択してください")
    public MultipartFile catImage3;

    private String image4;

    @ValidImageFile(groups = ImgValidGroup.class, message = "無効なファイル形式です。画像ファイルを選択してください")
    public MultipartFile catImage4;

    private String movie;

    @ValidVideoFile(groups = VideoValidGroup.class, message = "無効なファイル形式または容量が500MB以上です。動画ファイルを選択してください")
    public MultipartFile catMovie;

    @NotBlank(groups = PetColorValidGroup1.class, message = "猫の毛色を選択してください")
    private String color;

    private int recruitmentId;

    @Positive(groups = PositiveValidGroup3.class, message = "お住まいの都道府県を選択してください")
    private int prefectureName;

    private String memo;

    private int status;

    private Date createdDate;

    private Date updatedDate;

    public interface PositiveValidGroup1{}
    public interface PositiveValidGroup2{}
    public interface PositiveValidGroup3{}
    public interface PetNameValidGroup1{}
    public interface PetColorValidGroup1{}
    public interface ImgValidGroup{}
    public interface VideoValidGroup{}
}
