package com.sil.jmongoj.domain.user.controller;

import com.sil.jmongoj.domain.user.dto.UserDto;
import com.sil.jmongoj.domain.user.service.UserService;
import com.sil.jmongoj.global.response.ApiResponse;
import com.sil.jmongoj.global.util.UtilMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 회원
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UtilMessage utilMessage;

    /**
     * 회원 목록
     * @param model
     * @param search
     * @return
     */
    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public String userList(Model model, UserDto.Search search) {
        Page<UserDto.Response> users = userService.userList(search);
        model.addAttribute("users", users);
        return "user/userList";
    }

    /**
     * 회원 상세
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
     * 회원 등록화면
     * @return
     */
    @GetMapping("/create")
    public String createForm() {
        return "user/userCreate";
    }

    /**
     * 회원 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto.Response>> userCreate(@Valid UserDto.CreateRequest request) {
        log.debug(request.toString());
        UserDto.Response response = userService.userCreate(request);

        ApiResponse<UserDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.create", null), response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 회원 수정화면
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/{id}/modify")
    public String userModifyForm(Model model, @PathVariable String id) {
        UserDto.Response user = userService.userDetail(id);
        model.addAttribute("user", user);
        return "user/userModify";
    }

    /**
     * 휘원 수정
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto.Response>> userModify(@PathVariable String id
    , @Valid UserDto.ModifyRequest request) {
        UserDto.Response response = userService.userModify(id, request);
        log.info(response.toString());
        ApiResponse<UserDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.modify", null), response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * 회원 삭제
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto.Response>> userDelete(@PathVariable String id) {
        userService.userDelete(id);
        ApiResponse<UserDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.delete", null), null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}