package com.zerobase.oriticket.domain.post.repository;

import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.Likes;
import com.zerobase.oriticket.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByMember_MemberIdAndSalePost(Long memberId, Post salePost);

    Optional<Likes> findBySalePost_SalePostIdAndMember_MemberId(Long salePostId, Long memberId);

    List<Likes> findAllByMember_MemberIdAndSalePost_SaleStatusNotIn(Long memberId, List<SaleStatus> saleStatusList);
}
