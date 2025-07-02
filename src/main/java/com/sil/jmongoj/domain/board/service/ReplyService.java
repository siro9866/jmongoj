package com.sil.jmongoj.domain.board.service;

import com.sil.jmongoj.domain.board.dto.ReplyDto;
import com.sil.jmongoj.domain.board.entity.Reply;
import com.sil.jmongoj.domain.board.repository.ReplyRepository;
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
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UtilMessage utilMessage;

    /**
     * 목록
     * @param search
     * @return
     */
    public Page<ReplyDto.Response> replyList(ReplyDto.Search search) {
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Reply> replyPage = replyRepository.findByBoardIdAndEnabledTrue(search.getBoardId(), pageable);
        return replyPage.map(ReplyDto.Response::toDto);

    }

    /**
     * 등록
     * @param request
     * @return
     */
    public ReplyDto.Response replyCreate(ReplyDto.CreateRequest request) {

        Reply reply = replyRepository.findById(request.getBoardId())
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        replyRepository.save(request.toEntity());
        return ReplyDto.Response.toDto(reply);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    public ReplyDto.Response modifyReply(String id, ReplyDto.ModifyRequest request) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 권한확인(자기것만)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals(reply.getCreatedBy())){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.auth.forbidden", null));
        }

        request.modifyReply(reply);

        // MongoDB에 명시적으로 저장
        replyRepository.save(reply);
        return ReplyDto.Response.toDto(reply);
    }

    /**
     * 삭제
     * @param id
     */
    public void deleteReply(String id) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));

        // 권한확인(자기것만)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getName().equals(reply.getCreatedBy())){
            throw new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("reply.auth.forbidden", null));
        }

        replyRepository.deleteById(reply.getId());
    }
}