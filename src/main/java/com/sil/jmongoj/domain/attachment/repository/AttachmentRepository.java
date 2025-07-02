package com.sil.jmongoj.domain.attachment.repository;

import com.sil.jmongoj.domain.attachment.entity.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, String> {

    List<Attachment> findByParentTypeAndParentId(String parentType, String parentId);
}
