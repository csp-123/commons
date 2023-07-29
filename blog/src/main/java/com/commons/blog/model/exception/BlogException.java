package com.commons.blog.model.exception;

/**
 * 异常
 *
 * @author chishupeng
 * @date 2023/7/10 1:53 PM
 */
public class BlogException extends RuntimeException {
    private static final long serialVersionUID = 657378777056762471L;

    private String errorCode;
    public BlogException() {
        super();
    }

    public BlogException(String message) {
        super(message);
    }

    public BlogException(Throwable cause) {
        super(cause);
    }

    public BlogException(String message, Throwable cause) {
        super(message, cause);
    }
}