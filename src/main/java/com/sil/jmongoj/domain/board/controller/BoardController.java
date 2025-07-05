package com.sil.jmongoj.domain.board.controller;

import com.sil.jmongoj.domain.board.dto.BoardDto;
import com.sil.jmongoj.domain.board.service.BoardService;
import com.sil.jmongoj.global.response.ApiResponse;
import com.sil.jmongoj.global.util.UtilMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 게시판
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UtilMessage utilMessage;

    /**
     * 게시판 목록
     * @param search
     * @return
     */
    @GetMapping
    public String boardList(Model model, BoardDto.Search search) {
        Page<BoardDto.Response> boards = boardService.boardList(search);
        model.addAttribute("boards", boards);
        return "board/boardList";
    }

    /**
     * 게시판 상세
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String boardDetail(Model model, @PathVariable String id) {
        BoardDto.Response board = boardService.boardDetail(id);
        model.addAttribute("board", board);
        return "board/boardDetail";
    }

    /**
     * 게시판 등록화면
     * @return
     */
    @GetMapping("/create")
    public String createForm() {
        return "board/boardCreate";
    }


    /**
     * 게시판 등록
     * @param request
     * @param mFiles
     * @return
     * @throws IOException
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardDto.Response>> boardCreate(
            @Valid BoardDto.CreateRequest request,
            @RequestParam (name = "mFiles", required = false) MultipartFile[] mFiles
    ) throws IOException {
        BoardDto.Response board = boardService.boardCreate(request, mFiles);

        ApiResponse<BoardDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.create", null), board);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 게시판 수정화면
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/{id}/modify")
    public String boardModifyForm(Model model, @PathVariable String id) {
        BoardDto.Response board = boardService.boardDetail(id);
        model.addAttribute("board", board);
        return "board/boardModify";
    }

    /**
     * 게시판 수정
     * @param id
     * @param request
     * @param mFiles
     * @return
     * @throws IOException
     */
    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardDto.Response>> boardModify(@PathVariable String id
        , @Valid BoardDto.ModifyRequest request
        , @RequestParam(name = "mFiles", required = false) MultipartFile[] mFiles) throws IOException {
        BoardDto.Response board = boardService.boardModify(id, request, mFiles);
        log.info(board.toString());
        ApiResponse<BoardDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.modify", null), board);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * 게시판 삭제
     * @param id
     * @return
     * @throws IOException
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BoardDto.Response>> boardDelete(@PathVariable String id) throws IOException {
        boardService.boardDelete(id);
        ApiResponse<BoardDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.delete", null), null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}