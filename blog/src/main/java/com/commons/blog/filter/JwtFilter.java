package com.commons.blog.filter;

import com.alibaba.fastjson.JSON;
import com.commons.blog.model.constant.BlogRequestConstant;
import com.commons.blog.shiro.token.JwtToken;
import com.commons.blog.utils.RedisUtil;
import com.commons.core.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

import static com.commons.blog.model.constant.BlogRequestConstant.AUTHORIZATION_FIELD;

@Component
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    
    private String errorMsg;

    
    /**
     * 过滤器拦截请求的入口方法
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 判断请求头是否带上“Token”
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(AUTHORIZATION_FIELD);
        if (!StringUtils.hasText(token)) {
            // 游客访问开放资源可以不用携带 token
            StringBuilder uriPrefix = new StringBuilder();
            uriPrefix.append(BlogRequestConstant.BLOG)
                    .append(BlogRequestConstant.REQUEST_WEB_PREFIX);
            if (((HttpServletRequest) request).getRequestURI().startsWith(uriPrefix.toString())) {
                return true;
            }
            errorMsg = "请先登录";
            return false;
        }
        // 交给 myRealm
        Subject subject = SecurityUtils.getSubject();
        subject.login(new JwtToken(token));
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(400);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.println(JSON.toJSONString(Result.fail(errorMsg)));
        out.flush();
        out.close();
        return false;
    }

    /**
     * 对跨域访问提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域发送一个option请求
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

}
