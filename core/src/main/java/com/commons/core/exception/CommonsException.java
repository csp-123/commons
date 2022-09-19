package com.commons.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 异常类
 * @author: cccsp
 * @date: 2022/8/4 1:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonsException extends Throwable {

    private String errorCode;

    private String message;

}
