package com.sil.jmongoj.domain.board.repository;

import com.sil.jmongoj.domain.board.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    Page<Comment> findByBoardIdAndEnabledTrue(String boardId, Pageable pageable);
}