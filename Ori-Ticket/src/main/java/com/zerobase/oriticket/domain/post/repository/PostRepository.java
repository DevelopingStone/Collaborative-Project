package com.zerobase.oriticket.domain.post.repository;

import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMember_MemberIdAndSaleStatusIn(Long memberId, List<SaleStatus> saleStatusList);
}
