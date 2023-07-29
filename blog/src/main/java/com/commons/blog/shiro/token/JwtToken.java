package com.commons.blog.shiro.token;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author chishupeng
 * @date 2023/7/26 9:32 AM
 */
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

//    private String username;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
