package com.emrys.neondraw.common.interceptor;

import com.emrys.neondraw.common.utils.JWTUtil;
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
    private static final String USER_TOKEN_HEADER = "user_token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader(USER_TOKEN_HEADER);
        if (!StringUtils.hasText(token)) {
            log.info("当前token为空, uri={}", request.getRequestURI());
            response.setStatus(401);
            return false;
        }
        log.info("token:{}", token);
        Claims claims = JWTUtil.parseToken(token);
        if (claims == null) {
            log.info("token 已经失效, uri={}", request.getRequestURI());
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
