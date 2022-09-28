package com.commons.core.exception;

import lombok.*;

/**
 * @description: 异常类
 * @author: cccsp
 * @date: 2022/8/4 1:22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonsException extends Throwable {

    private String errorCode;

    private String message;

}
