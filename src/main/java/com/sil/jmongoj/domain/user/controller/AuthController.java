package com.sil.jmongoj.domain.user.controller;

import com.sil.jmongoj.domain.user.dto.UserDto;
import com.sil.jmongoj.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/auth/login")
    public String loginForm() {
        log.info("loginForm");
        return "auth/login";
    }

    /**
     * 회원가입
     * @param request
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<UserDto.Response> signup(@ModelAttribute @Valid UserDto.CreateRequest request) {
        // SWAGGER에 form타입으로 나오려고 위처럼변경함 @RequestBody @Valid UserDto.CreateRequest
        log.debug(request.toString());
        UserDto.Response response = userService.userCreate(request);
        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "로그인", description = "사용자 로그인")
//    @PostMapping("/login")
//    public ResponseEntity<Void> login(@RequestBody UserDto.LoginRequest request) {
//        // Spring Security에서 처리하므로 여기선 더미 응답
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//    @Operation(summary = "로그아웃", description = "사용자 로그아웃")
//    @PostMapping("/logout")
//    public ResponseEntity<Void> logout() {
//        return ResponseEntity.ok().build();
//    }

}