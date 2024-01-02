package com.example.nyander.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ValidImageFile, MultipartFile> {
    @Override
    public void initialize(ValidImageFile constraintAnnotation) {
        // 必要に応じて初期化する
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // ファイルが空の場合はバリデーションをパス
        }

        // 拡張子をチェックしてファイルが画像形式であることをチェックする
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/gif"));
    }
}
