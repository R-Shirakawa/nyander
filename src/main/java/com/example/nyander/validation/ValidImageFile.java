package com.example.nyander.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageFileValidator.class)
public @interface ValidImageFile {
    String message() default "画像ファイルのみ選択可能です。有効な画像ファイルを選択してください";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

