package com.emrys.lotterysystem.common.interceptor;

import com.emrys.lotterysystem.common.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Emrys
 * content:
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private static final String USER_TOKEN = "user_token";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从请求头中获取token
        String token = request.getHeader("USER_TOKEN");
        if(!StringUtils.hasText(token)){
            log.info("当前token为空");
            response.setStatus(401);
            return false;
        }
        log.info("token:{}",token);
        //解析token
        Claims claims = JWTUtil.parseToken(token);
        if(claims == null){
            log.info("token 已经失效");
            response.setStatus(401);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
