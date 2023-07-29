package com.commons.blog.model.annotation;

import com.commons.blog.model.enums.SourceTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SourceTypeAnno {
    SourceTypeEnum type();
}
