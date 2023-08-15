package com.revo.application.interceptor;

import com.revo.application.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class GetCurrentUserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws Exception {
        String credentials = request.getHeader("_user");
        User user = this.extractFrom(credentials);
        request.setAttribute("user", user);
        return true;
    }

    private User extractFrom(String json) {
        String[] rawData = json.split(";", 3);
        String userId = rawData[0];
        String name = rawData[1];
        String email = rawData[2];

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setUserId(userId);

        return user;
    }
}
