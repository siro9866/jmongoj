package com.sil.jmongoj.domain.user.controller;

import com.sil.jmongoj.domain.user.dto.UserDto;
import com.sil.jmongoj.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "회원", description = "회원 API")
public class UserController {

    private final UserService userService;

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public String userList(Model model, @ParameterObject @ModelAttribute UserDto.Search search) {
        Page<UserDto.Response> users = userService.userList(search);
        model.addAttribute("users", users);
        return "user/userList";
    }

    @GetMapping("/{id}")
    public String userDetail(Model model, @PathVariable String id) {
        UserDto.Response user = userService.userDetail(id);
        model.addAttribute("user", user);
        return "user/userDetail";
    }

    @Operation(summary = "회원수정", description = "회원수정")
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserDto.Response> userModify(@PathVariable String id
    , @ParameterObject @ModelAttribute @Valid UserDto.ModifyRequest request) {
        userService.userModify(id, request);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "회원삭제", description = "회원삭제")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserDto.Response> userDelete(@PathVariable String id) {
        userService.userDelete(id);
        return ResponseEntity.ok(null);
    }
}