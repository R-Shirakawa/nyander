package com.example.nyander.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VideoFileValidator implements ConstraintValidator<ValidVideoFile, MultipartFile> {

    private static final long MAX_VIDEO_SIZE = 5_000_000_000L; // 500MB

    @Override
    public void initialize(ValidVideoFile constraintAnnotation) {
        // 必要に応じて初期化する
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // ファイルが空の場合はバリデーションをパス
        }

        // 動画形式のチェック
        String contentType = file.getContentType();
        if (contentType != null && (contentType.equals("video/mp4") || contentType.equals("video/mov") || contentType.equals("video/wmv") || contentType.equals("video/avi"))) {
            // 動画ファイルのサイズが1MB以下であることを確認
            return file.getSize() <= MAX_VIDEO_SIZE;
        }
        return false; // 動画形式でない場合はバリデーションを失敗させる
        // 拡張子をチェックしてファイルが動画形式であることをチェックする
//        String contentType = file.getContentType();
//        return contentType != null && (contentType.equals("video/mp4") || contentType.equals("video/mov") || contentType.equals("video/wmv") || contentType.equals("video/avi"));
//    }
    }
}