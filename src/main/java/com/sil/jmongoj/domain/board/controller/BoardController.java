package com.sil.jmongoj.domain.board.controller;

import com.sil.jmongoj.domain.board.dto.BoardDto;
import com.sil.jmongoj.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시판", description = "게시판 API")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판목록", description = "게시판목록")
    @GetMapping
    public ResponseEntity<Page<BoardDto.Response>> boardList(@ModelAttribute BoardDto.Search search) {
        Page<BoardDto.Response> boards = boardService.boardList(search);
        return ResponseEntity.ok(boards);
    }

    @Operation(summary = "게시판상세", description = "게시판상세")
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto.Response> boardDetail(@PathVariable String id) {
        BoardDto.Response board = boardService.boardDetail(id);
        return ResponseEntity.ok(board);
    }

    @Operation(summary = "게시판등록", description = "게시판등록")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDto.Response> boardCreate(
            @RequestPart @ModelAttribute @Valid BoardDto.CreateRequest request,
            @RequestPart(name = "files", required = false) MultipartFile[] files
    ) throws IOException {
        BoardDto.Response board = boardService.boardCreate(request, files);
        return ResponseEntity.ok(board);
    }

    @Operation(summary = "게시판수정", description = "게시판수정")
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardDto.Response> boardModify(@PathVariable String id
        , @ModelAttribute @Valid BoardDto.ModifyRequest request
        , @RequestPart(name = "files", required = false) MultipartFile[] files) throws IOException {
        boardService.boardModify(id, request, files);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "게시판삭제", description = "게시판삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<BoardDto.Response> commentDelete(@PathVariable String id) throws IOException {
        boardService.commentDelete(id);
        return ResponseEntity.ok(null);
    }
}