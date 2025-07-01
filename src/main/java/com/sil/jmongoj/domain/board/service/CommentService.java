package com.sil.jmongoj.domain.board.service;

import com.sil.jmongoj.domain.board.dto.CommentDto;
import com.sil.jmongoj.domain.board.entity.Comment;
import com.sil.jmongoj.domain.board.repository.CommentRepository;
import com.sil.jmongoj.global.exception.CustomException;
import com.sil.jmongoj.global.response.ResponseCode;
import com.sil.jmongoj.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UtilMessage utilMessage;

    /**
     * 목록
     * @param search
     * @return
     */
    public Page<CommentDto.Response> commentList(CommentDto.Search search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Comment> commentPage = commentRepository.findByBoardIdAndEnabledTrue(search.getBoardId(), pageable);
        return commentPage.map(CommentDto.Response::toDto);

    }

    /**
     * 등록
     * @param request
     * @return
     */
    public CommentDto.Response commentCreate(CommentDto.CreateRequest request) {

        Comment comment = commentRepository.findById(request.getBoardId())
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        commentRepository.save(request.toEntity());
        return CommentDto.Response.toDto(comment);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    public CommentDto.Response modifyComment(String id, CommentDto.ModifyRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 권한확인(자기것만)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals(comment.getCreatedBy())){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("comment.auth.forbidden", null));
        }

        request.modifyComment(comment);

        // MongoDB에 명시적으로 저장
        commentRepository.save(comment);
        return CommentDto.Response.toDto(comment);
    }

    /**
     * 삭제
     * @param id
     */
    public void deleteComment(String id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 권한확인(자기것만)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals(comment.getCreatedBy())){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("comment.auth.forbidden", null));
        }

        commentRepository.deleteById(comment.getId());
    }
}