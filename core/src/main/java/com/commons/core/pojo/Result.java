package com.commons.core.pojo;

import com.google.common.base.MoreObjects;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(Result.class);

    /**
     * 调用是否成功
     */
    private boolean success;

    /**
     * 如果success = true,则通过result可以获得调用结果
     */
    private T result;

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String message;


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("success", success)
                .add("result", result)
                .add("code", code)
                .omitNullValues()
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Result)) return false;
        Result other = (Result) o;
        if (!Objects.equals(this.success, other.success)) return false;
        if (!Objects.equals(this.result, other.result)) return false;
        if (!Objects.equals(this.code, other.code)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result * PRIME) + (this.success ? 1 : 0);
        result = (result * PRIME) + (this.result == null ? 0 : this.result.hashCode());
        result = (result * PRIME) + (this.code == null ? 0 : this.code.hashCode());
        return result;
    }


    public static <T> Result<T> success(T data) {
        Result<T> resp = new Result<>();
        resp.setResult(data);
        resp.setCode("200");
        resp.setSuccess(true);
        return resp;
    }

    public static <T> Result<T> success() {
        return Result.success(null);
    }

    public static <T> Result<T> fail(String error) {
        Result<T> resp = new Result<>();
        resp.setSuccess(false);
        resp.setMessage(error);
        return resp;
    }

    /**
     * usage: Result&lt;String&gt; resp = Result.get(() -> someDAO.getSomeStringResult(), "error.code")
     * <br>
     * 这个封装有个不太好的地方，无法打印出自定义错误日志……
     *
     * @param supplier  lambda
     * @param errorCode error code
     * @param <T>       anything
     * @return Result
     */
    public static <T> Result<T> get(Supplier<T> supplier, String errorCode) {
        try {
            T result = supplier.get();
            return Result.success(result);
        } catch (Exception e) {
            logger.error("error when get response call", e);
            return Result.fail(errorCode);
        }
    }

    public static <T> Result<T> fail(String code, String error) {
        return new Result<>();
    }

    public Result<T> code(String code) {
        this.code = code;
        return this;
    }
}
