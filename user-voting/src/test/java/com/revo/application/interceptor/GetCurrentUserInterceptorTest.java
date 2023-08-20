package com.revo.application.interceptor;

import com.revo.application.entity.User;
import com.revo.application.exception.UnknownUserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class GetCurrentUserInterceptorTest {
    private GetCurrentUserInterceptor interceptor;
    private MockHttpServletRequest request;
    private HttpServletResponse response;

    private final Object emptyObj = new Object();

    @BeforeEach
    void setUp() {
        this.interceptor = new GetCurrentUserInterceptor();
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    @Test
    void testEmptyUserInHeader() {
        Assertions.assertThrows(UnknownUserException.class, () -> {
            interceptor.preHandle(request, response, emptyObj);
        });
    }

    @Test
    void testInvalidUserHeaderCredentials() {
        this.request.addHeader("_user", "userid;samuel");

        Assertions.assertThrows(UnknownUserException.class, () -> {
            interceptor.preHandle(request, response, emptyObj);
        });
    }

    @Test
    void testValidUserHeader() throws Exception {
        this.request.addHeader("_user", "userid;samuel;samuel@gmail.com");
        var handle = this.interceptor.preHandle(request, response, emptyObj);

        Assertions.assertTrue(handle);

        var expected = new User();
        expected.setName("samuel");
        expected.setEmail("samuel@gmail.com");
        expected.setUserId("userid");

        var result = (User) this.request.getAttribute("user");
        Assertions.assertNotNull(result);

        Assertions.assertAll("reflect object",
                () -> Assertions.assertEquals(expected.getUserId(), result.getUserId()),
                () -> Assertions.assertEquals(expected.getEmail(), result.getEmail()),
                () -> Assertions.assertEquals(expected.getName(), result.getName())
        );
    }
}