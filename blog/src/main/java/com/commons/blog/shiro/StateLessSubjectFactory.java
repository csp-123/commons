package com.commons.blog.shiro;

import com.commons.blog.shiro.token.JwtToken;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
     * 扩展自DefaultWebSubjectFactory,对于无状态的TOKEN 类型不创建session
     * @author zqf
     * @date 11/27/2018
     */
    public class StateLessSubjectFactory extends DefaultWebSubjectFactory {

        @Override
        public Subject createSubject(SubjectContext context) {
            AuthenticationToken token = context.getAuthenticationToken();
            if((token instanceof JwtToken)){
                // 当token为HmacToken时， 不创建 session
                context.setSessionCreationEnabled(false);
            }
            return super.createSubject(context);
        }
    }