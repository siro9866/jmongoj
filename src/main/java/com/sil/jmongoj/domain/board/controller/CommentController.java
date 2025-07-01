package com.sil.jmongoj.domain.board.controller;

import com.sil.jmongoj.domain.board.dto.CommentDto;
import com.sil.jmongoj.domain.board.service.CommentService;
import com.sil.jmongoj.domain.user.dto.UserDto;
import com.sil.jmongoj.global.response.ApiResponse;
import com.sil.jmongoj.global.util.UtilMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 댓글
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UtilMessage utilMessage;

    /**
     * 댓글 목록
     * @param search
     * @return
     */
    @GetMapping("/board/{boardId}")
    public String commentList(Model model, CommentDto.Search search) {
        Page<CommentDto.Response> comments = commentService.commentList(search);
        model.addAttribute("comments", comments);
        return "board/commentList";
    }

    /**
     * 댓글 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CommentDto.Response>> commentCreate(@Valid CommentDto.CreateRequest request) {
        CommentDto.Response response = commentService.commentCreate(request);

        ApiResponse<CommentDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.create", null), response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 댓글 수정
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDto.Response>> boardModify(@PathVariable String id,
        @Valid CommentDto.ModifyRequest request) {
        CommentDto.Response response = commentService.modifyComment(id, request);
        log.info(response.toString());
        ApiResponse<CommentDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.modify", null), response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * 댓글 삭제
     * @param id
     * @return
     * @throws IOException
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentDto.Response>> commentDelete(@PathVariable String id) {
        commentService.deleteComment(id);
        ApiResponse<CommentDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.delete", null), null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}