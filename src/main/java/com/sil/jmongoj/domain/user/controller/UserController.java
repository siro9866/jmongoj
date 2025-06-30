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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * 회원목록
     * @param model
     * @param search
     * @return
     */
    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public String userList(Model model, @ModelAttribute UserDto.Search search) {
        Page<UserDto.Response> users = userService.userList(search);
        model.addAttribute("users", users);
        return "user/userList";
    }

    /**
     * 회원상세
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String userDetail(Model model, @PathVariable String id) {
        UserDto.Response user = userService.userDetail(id);
        model.addAttribute("user", user);
        return "user/userDetail";
    }

    /**
     * 회원등록화면
     * @return
     */
    @GetMapping("/create")
    public String createForm() {
        return "user/userCreateForm";
    }

    /**
     * 회원등록
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<UserDto.Response> userCreate(@ModelAttribute @Valid UserDto.CreateRequest request) {
        log.debug(request.toString());
        UserDto.Response response = userService.userCreate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 수정화면
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/{id}/modify")
    public String modifyForm(Model model, @PathVariable String id) {
        UserDto.Response user = userService.userDetail(id);
        model.addAttribute("user", user);
        return "user/userModifyForm";
    }

    /**
     * 휘원수정
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserDto.Response> userModify(@PathVariable String id
    , @ModelAttribute @Valid UserDto.ModifyRequest request) {
        userService.userModify(id, request);
        return ResponseEntity.ok(null);
    }

    /**
     * 회원삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserDto.Response> userDelete(@PathVariable String id) {
        userService.userDelete(id);
        return ResponseEntity.ok(null);
    }
}