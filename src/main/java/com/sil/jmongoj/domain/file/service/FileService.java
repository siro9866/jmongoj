package com.sil.jmongoj.domain.file.service;

import com.sil.jmongoj.domain.file.dto.FileDto;
import com.sil.jmongoj.domain.file.entity.File;
import com.sil.jmongoj.domain.file.repository.FileRepository;
import com.sil.jmongoj.global.code.ParentType;
import com.sil.jmongoj.global.exception.CustomException;
import com.sil.jmongoj.global.response.ResponseCode;
import com.sil.jmongoj.global.util.UtilCommon;
import com.sil.jmongoj.global.util.UtilFile;
import com.sil.jmongoj.global.util.UtilMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final MongoTemplate mongoTemplate;
    private final FileRepository fileRepository;
    private final UtilMessage utilMessage;

    @Value("${custom.format.dateStr}") private String FORMAT_DATESTR;

    // BOARD 게시판
    @Value("${custom.file.board.dir}") private String FILE_BOARD_DIR;
    @Value("${custom.file.board.path}") private String FILE_BOARD_PATH;

    /**
     * 목록
     * @param search
     * @return
     */
    public List<FileDto.Response> fileList(FileDto.Search search) {
        List<File> files = fileRepository.findByParentTypeAndParentId(search.getParentType().name(), search.getParentId());
        return files.stream()
                .map(FileDto.Response::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 상세
     * @param id
     * @return
     */
    public FileDto.Response fileDetail(String id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        return FileDto.Response.toDto(file);
    }

    /**
     * 등록
     * @param request
     * @return
     */
    public List<FileDto.Response> fileCreate(FileDto.CreateBaseRequest request, MultipartFile[] mFiles) throws IOException {
        List<FileDto.Response> fileResponses = new ArrayList<>();

        // 물리 파일저장경로
        String file_dir;
        String file_path;
        if (Objects.requireNonNull(request.getParentType()) == ParentType.BOARD) {
            file_dir = FILE_BOARD_DIR;
            file_path = FILE_BOARD_PATH;
        } else {
            file_dir = FILE_BOARD_DIR;
            file_path = FILE_BOARD_PATH;
        }

        // 파일업로드
        if(UtilCommon.isNotEmpty(mFiles)) {
            FileDto.CreateRequest createRequest;
            StringBuilder systemFileName;
            for(MultipartFile mFile : mFiles) {
                systemFileName = new StringBuilder();
                systemFileName.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern(FORMAT_DATESTR)));
                systemFileName.append("_");
                systemFileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
                systemFileName.append(".");
                systemFileName.append(FilenameUtils.getExtension(mFile.getOriginalFilename()));

                createRequest = new FileDto.CreateRequest();
                createRequest.setUploadPath(file_path);
                createRequest.setOrgFileName(mFile.getOriginalFilename());
                createRequest.setSysFileName(systemFileName.toString());
                createRequest.setParentType(request.getParentType());
                createRequest.setParentId(request.getParentId());

                // 파일저장
                File file = fileRepository.save(createRequest.toEntity());
                fileResponses.add(FileDto.Response.toDto(file));

                UtilFile.makeFolders(file_dir + file_path);
                String filePath = file_dir + file_path + java.io.File.separator + systemFileName;
                log.info("upload file: {}", filePath);
                mFile.transferTo(new java.io.File(filePath));
            }
        }

        return fileResponses;
    }

    /**
     * 삭제
     * @param request
     */
    public void fileDelete(FileDto.DeleteRequest request) throws IOException {
        File file = fileRepository.findById(request.getId())
                .orElseThrow(() -> new CustomException(ResponseCode.EXCEPTION_NODATA, utilMessage.getMessage("notfound.data", null)));
        fileRepository.deleteById(file.getId());

        // 물리 파일저장경로
        String file_dir;
        String file_path;
        if (Objects.requireNonNull(request.getParentType()).equals(ParentType.BOARD)) {
            file_dir = FILE_BOARD_DIR;
        } else {
            file_dir = FILE_BOARD_DIR;
        }

        // 실제파일삭제
        Path filePath = Paths.get(file_dir + file.getUploadPath() + java.io.File.separator + file.getSysFileName());
        Files.deleteIfExists(filePath);
    }

    /**
     * 삭제
     * @param request
     */
    public void fileDeleteAll(FileDto.DeleteRequest request) throws IOException {
        List<File> files = fileRepository.findByParentTypeAndParentId(request.getParentType().name(), request.getParentId());
        fileRepository.deleteAll(files);

        // 물리 파일저장경로
        String file_dir;
        String file_path;
        if (Objects.requireNonNull(request.getParentType()).equals(ParentType.BOARD)) {
            file_dir = FILE_BOARD_DIR;
        } else {
            file_dir = FILE_BOARD_DIR;
        }

        // 실제파일삭제
        files.forEach(file -> {
            Path filePath = Paths.get(file_dir + file.getUploadPath() + java.io.File.separator + file.getSysFileName());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
