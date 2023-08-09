package com.commons.blog.handler;

import com.commons.core.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.assertj.core.util.Throwables;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截器
 *
 * @author chishupeng
 * @date 2023/7/25 7:44 PM
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handler(RuntimeException e) {
        log.error("发生异常：" + Throwables.getStackTrace(e));
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(ShiroException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Result handler(ShiroException e) {
        log.error("登录或权限异常：" + Throwables.getStackTrace(e));
        return Result.fail(e.getMessage());
    }

}
