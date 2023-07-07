package com.commons.core.pojo;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

@Data
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(Response.class);

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
     * 如果success = false,则通过error可以查看错误信息
     */
    private String error;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("success", success)
                .add("result", result)
                .add("code", code)
                .add("error", error)
                .omitNullValues()
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Response)) return false;
        Response other = (Response) o;
        if (!Objects.equals(this.success, other.success)) return false;
        if (!Objects.equals(this.result, other.result)) return false;
        if (!Objects.equals(this.code, other.code)) return false;
        if (!Objects.equals(this.error, other.error)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result * PRIME) + (this.success ? 1 : 0);
        result = (result * PRIME) + (this.result == null ? 0 : this.result.hashCode());
        result = (result * PRIME) + (this.code == null ? 0 : this.code.hashCode());
        result = (result * PRIME) + (this.error == null ? 0 : this.error.hashCode());
        return result;
    }


    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response<>();
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> ok() {
        return Response.ok(null);
    }

    public static <T> Response<T> fail(String error) {
        Response<T> resp = new Response<>();
        resp.setError(error);
        return resp;
    }

    /**
     * usage: Response&lt;String&gt; resp = Response.get(() -> someDAO.getSomeStringResult(), "error.code")
     * <br>
     * 这个封装有个不太好的地方，无法打印出自定义错误日志……
     *
     * @param supplier  lambda
     * @param errorCode error code
     * @param <T>       anything
     * @return Response
     */
    public static <T> Response<T> get(Supplier<T> supplier, String errorCode) {
        try {
            T result = supplier.get();
            return Response.ok(result);
        } catch (Exception e) {
            logger.error("error when get response call", e);
            return Response.fail(errorCode);
        }
    }

    public static <T> Response<T> fail(String code, String error) {
        return new Response<>();
    }

    public Response<T> code(String code) {
        this.code = code;
        return this;
    }
}
