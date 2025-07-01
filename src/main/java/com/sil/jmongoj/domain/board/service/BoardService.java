package com.sil.jmongoj.domain.board.service;

import com.sil.jmongoj.domain.board.dto.BoardDto;
import com.sil.jmongoj.domain.board.entity.Board;
import com.sil.jmongoj.domain.board.repository.BoardRepository;
import com.sil.jmongoj.domain.file.dto.FileDto;
import com.sil.jmongoj.domain.file.service.FileService;
import com.sil.jmongoj.global.code.ParentType;
import com.sil.jmongoj.global.exception.CustomException;
import com.sil.jmongoj.global.response.ResponseCode;
import com.sil.jmongoj.global.util.UtilCommon;
import com.sil.jmongoj.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final UtilMessage utilMessage;

    /**
     * 목록
     * @param search
     * @return
     */
    public Page<BoardDto.Response> boardList(BoardDto.Search search) {
        Query query = new Query();

        // 🔍 키워드 like 검색 (boardname, email, name 중 하나라도 포함)
        if (UtilCommon.isNotEmpty(search.getKeyword())) {
            String keyword = search.getKeyword();
            Criteria keywordCriteria = new Criteria().orOperator(
                    Criteria.where("title").regex(keyword, "i"),
                    Criteria.where("content").regex(keyword, "i")
            );
            query.addCriteria(keywordCriteria);
        }

        // 📅 등록일자 조건
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (UtilCommon.isNotEmpty(search.getFromDate()) && UtilCommon.isNotEmpty(search.getToDate())) {
            LocalDateTime from = LocalDate.parse(search.getFromDate(), formatter).atStartOfDay();
            LocalDateTime to = LocalDate.parse(search.getToDate(), formatter).atTime(23, 59, 59);
            query.addCriteria(Criteria.where("createdAt").gte(from).lte(to));
        }

        // 📦 페이징 + 정렬
        Sort.Direction direction = search.isDesc() ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(direction, search.getSortBy()));
        query.with(pageable);

        // ✨ 실행
        List<Board> boards = mongoTemplate.find(query, Board.class);
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Board.class);

        // DTO 변환
        List<BoardDto.Response> content = boards.stream()
                .map(BoardDto.Response::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 상세
     * @param id
     * @return
     */
    public BoardDto.Response boardDetail(String id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        BoardDto.Response response = BoardDto.Response.toDto(board);

        FileDto.Search fileSearch = new FileDto.Search();
        fileSearch.setParentType(ParentType.BOARD);
        fileSearch.setParentId(board.getId());
        List<FileDto.Response> files = fileService.fileList(fileSearch);

        response.setFiles(files);
        return response;
    }

    /**
     * 등록
     * @param request
     * @return
     */
    public BoardDto.Response boardCreate(BoardDto.CreateRequest request, MultipartFile[] mFiles) throws IOException {
        Board board = boardRepository.save(request.toEntity());

        // 파일저장
        if(UtilCommon.isNotEmpty(mFiles)) {
            FileDto.CreateBaseRequest baseRequest = new FileDto.CreateBaseRequest();
            baseRequest.setParentType(ParentType.BOARD);
            baseRequest.setParentId(board.getId());

            fileService.fileCreate(baseRequest, mFiles);
        }

        return BoardDto.Response.toDto(board);
    }

    /**
     * 수정
     * @param id
     * @param request
     */
    public BoardDto.Response boardModify(String id, BoardDto.ModifyRequest request, MultipartFile[] mFiles) throws IOException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        request.boardModify(board);

        // MongoDB에 명시적으로 저장
        boardRepository.save(board);

        // UI상에서 삭제된 파일은 삭제처리해야함
        // 파일정보삭제
        FileDto.DeleteRequest fileDeleteRequest;
        if(UtilCommon.isNotEmpty(request.getFileIds())) {
            for(String fileId : request.getFileIds()) {
                fileDeleteRequest = new FileDto.DeleteRequest();
                fileDeleteRequest.setParentType(ParentType.BOARD);
                fileDeleteRequest.setId(fileId);
                fileService.fileDelete(fileDeleteRequest);
            }
        }

        // 파일저장
        if(UtilCommon.isNotEmpty(mFiles)) {
            FileDto.CreateBaseRequest baseRequest = new FileDto.CreateBaseRequest();
            baseRequest.setParentType(ParentType.BOARD);
            baseRequest.setParentId(board.getId());

            fileService.fileCreate(baseRequest, mFiles);
        }
        return BoardDto.Response.toDto(board);
    }

    /**
     * 삭제
     * @param id
     */
    public void boardDelete(String id) throws IOException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        boardRepository.deleteById(board.getId());

        // 파일 삭제
        FileDto.DeleteRequest fileDeleteRequest = new FileDto.DeleteRequest();
        fileDeleteRequest.setParentType(ParentType.BOARD);
        fileDeleteRequest.setParentId(board.getId());
        fileService.fileDeleteAll(fileDeleteRequest);
    }
}