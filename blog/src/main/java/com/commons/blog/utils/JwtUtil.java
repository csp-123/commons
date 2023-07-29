package com.commons.blog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * jwt工具类
 *
 * @author chishupeng
 * @date 2023/7/25 7:53 PM
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    @Setter
    private String secret;

    public String createJwtToken(String username, long expireTime, TimeUnit timeUnit) {
        Date date = new Date(System.currentTimeMillis() + timeUnit.toMillis(expireTime));
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }


    public String createJwtToken(String username) {
        Date date = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10));
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 判断是否过期
     */
    public boolean isExpire(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().getTime() < System.currentTimeMillis();
    }

    /**
     * 校验token是否正确
     */
    public boolean verifyToken(String token) throws TokenExpiredException {
        //user要从sercurityManager拿，确保用户用的是自己的token
        log.info("verifyToken");

        //根据密钥生成JWT效验器
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("username", getUsername(token))//从不加密的消息体中取出username
                .build();
        //生成的token会有roles的Claim，这里不加不知道行不行。
        // 一个是直接从客户端传来的token，一个是根据盐和用户名等信息生成secret后再生成的token
        DecodedJWT jwt = verifier.verify(token);
        //能走到这里
        return true;

    }

    /**
     * 在token中获取到username信息
     */
    public  String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
