package com.sil.jmongoj.domain.board.controller;

import com.sil.jmongoj.domain.board.dto.CommentDto;
import com.sil.jmongoj.domain.board.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "게시판 댓글", description = "게시판 댓글 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 목록", description = "댓글 목록")
    @GetMapping("/board/{boardId}")
    public ResponseEntity<Page<CommentDto.Response>> commentList(@ModelAttribute CommentDto.Search search) {
        Page<CommentDto.Response> comments = commentService.commentList(search);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "댓글 등록", description = "댓글 등록")
    @PostMapping
    public ResponseEntity<CommentDto.Response> commentCreate(@ModelAttribute @Valid CommentDto.CreateRequest request) {
        CommentDto.Response comment = commentService.commentCreate(request);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정")
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto.Response> boardModify(@PathVariable String id,
        @ModelAttribute @Valid CommentDto.ModifyRequest request) {
        commentService.modifyComment(id, request);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentDto.Response> commentDelete(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok(null);
    }
}