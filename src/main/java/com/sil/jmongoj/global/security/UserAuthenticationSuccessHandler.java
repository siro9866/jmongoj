package com.sil.jmongoj.global.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sil.jmongoj.domain.user.repository.UserRepository;
import com.sil.jmongoj.global.util.UtilMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 로그인 성공시 핸들러
 */
@Slf4j
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 생성자로 하면 secureconfig 에서 문제생겨서 걍 했음
    private final UserRepository userRepository;
    private final UtilMessage utilMessage;

    public UserAuthenticationSuccessHandler(UserRepository userRepository, UtilMessage utilMessage) {
        this.userRepository = userRepository;
        this.utilMessage = utilMessage;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        super.onAuthenticationSuccess(request, response, authentication);

        // TODO: 성공시 할거 세션등록등
        log.debug("onAuthenticationSuccess 아싸 로그인 성공했다");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("message", utilMessage.getMessage("login.success"));
//        responseData.put("redirectUrl", "/");

        // 자동 리다이렉션
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            responseData.put("redirectUrl", targetUrl);
        } else {
            responseData.put("redirectUrl", "/");
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));

//        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
//        String targetUrl = savedRequest != null ? savedRequest.getRedirectUrl() : "/";
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
