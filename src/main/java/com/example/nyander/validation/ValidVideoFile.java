package com.example.nyander.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

 @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Constraint(validatedBy = VideoFileValidator.class)
    public @interface ValidVideoFile {
        String message() default "動画ファイルのみ選択可能です。有効な動画ファイルを選択してください";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
