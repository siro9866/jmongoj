package com.sil.jmongoj.domain.board.controller;

import com.sil.jmongoj.domain.board.dto.ReplyDto;
import com.sil.jmongoj.domain.board.service.ReplyService;
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

import java.io.IOException;

/**
 * 댓글
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;
    private final UtilMessage utilMessage;

    /**
     * 댓글 목록
     * @param search
     * @return
     */
    @GetMapping("/board/{boardId}")
    public String replyList(Model model, ReplyDto.Search search) {
        Page<ReplyDto.Response> replys = replyService.replyList(search);
        model.addAttribute("replys", replys);
        return "board/replyList";
    }

    /**
     * 댓글 등록
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ReplyDto.Response>> replyCreate(@Valid ReplyDto.CreateRequest request) {
        ReplyDto.Response response = replyService.replyCreate(request);

        ApiResponse<ReplyDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.create", null), response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    /**
     * 댓글 수정
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReplyDto.Response>> boardModify(@PathVariable String id,
                                                                      @Valid ReplyDto.ModifyRequest request) {
        ReplyDto.Response response = replyService.modifyReply(id, request);
        log.info(response.toString());
        ApiResponse<ReplyDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.modify", null), response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    /**
     * 댓글 삭제
     * @param id
     * @return
     * @throws IOException
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ReplyDto.Response>> replyDelete(@PathVariable String id) {
        replyService.deleteReply(id);
        ApiResponse<ReplyDto.Response> apiResponse = new ApiResponse<>(utilMessage.getMessage("success.delete", null), null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}