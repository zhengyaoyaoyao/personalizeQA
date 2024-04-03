package com.personalize.personalizeqa.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.personalize.personalizeqa.dto.UserDTO;
import com.personalize.personalizeqa.entity.R;
import com.personalize.personalizeqa.entity.User;
import com.personalize.personalizeqa.server.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //get token
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
//            System.out.println("OPTIONS请求，放行");
            return true;
        }
        String token = request.getHeader("Authorization");
//        System.out.println("非OPTIONS请求，拦截");
        if (StrUtil.isBlank(token)) {
            response.setStatus(401);
            return false;
        }
        String key =RedisConstants.LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        if (userMap.isEmpty()){
            response.setStatus(401);
            return false;
        }
        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
        UserHolder.saveUser(userDTO);
//        String actionName = request.getRequestURI();
////        if (StrUtil.equals(userDTO.getPreAction(),"noAction")||StrUtil.equals(actionName,"/user/profile")||StrUtil.equals(actionName,userDTO.getPreAction())){
////            log.info("日志信息属于noAction、profile、与前面日志信息相同");
//        if (StrUtil.equals(actionName,"/user/profile")){
//            log.info("日志信息属于noAction、profile、与前面日志信息相同");
//        }else {
//            //无操作+和前操作不同，则添加
//            log.info("之前的操作,后来的操作:{}",actionName);
//            logService.insertLog(actionName);
//        }
        //刷新token有效期
        stringRedisTemplate.expire(key,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        System.out.println("消除用户信息");
        UserHolder.removeUser();
    }
}
