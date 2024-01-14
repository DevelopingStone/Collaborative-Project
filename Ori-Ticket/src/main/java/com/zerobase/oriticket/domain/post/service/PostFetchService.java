package com.zerobase.oriticket.domain.post.service;

import com.zerobase.oriticket.domain.post.constants.SaleStatus;
import com.zerobase.oriticket.domain.post.entity.Post;
import com.zerobase.oriticket.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostFetchService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<Post> get(Long memberId, List<SaleStatus> saleStatusList) {
        return postRepository.findAllByMember_MemberIdAndSaleStatusIn(memberId, saleStatusList);
    }
}
