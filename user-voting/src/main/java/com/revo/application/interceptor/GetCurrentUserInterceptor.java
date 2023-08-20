package com.revo.application.interceptor;

import com.revo.application.entity.User;
import com.revo.application.exception.UnknownUserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class GetCurrentUserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) throws UnknownUserException {
        String fromHeader = request.getHeader("_user");
        String credentials = Optional
                .ofNullable(fromHeader)
                .orElseThrow(UnknownUserException::new);

        User user = this.extractFrom(credentials);
        request.setAttribute("user", user);
        return true;
    }

    private User extractFrom(String s) throws UnknownUserException {
        String[] rawData = s.split(";", 3);
        if(rawData.length != 3) {
            throw new UnknownUserException();
        }

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
