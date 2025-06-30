package com.sil.jmongoj.domain.board.repository;

import com.sil.jmongoj.domain.board.entity.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends MongoRepository<Board, String> {
}