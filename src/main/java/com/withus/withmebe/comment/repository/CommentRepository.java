package com.withus.withmebe.comment.repository;

import com.withus.withmebe.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query(value = "select c from Comment c join fetch c.member where c.gatheringId = :gatheringId", countQuery = "select count(c) from Comment c where c.gatheringId = :gatheringId")
  Page<Comment> findCommentsByGatheringId(long gatheringId, Pageable pageable);

}
