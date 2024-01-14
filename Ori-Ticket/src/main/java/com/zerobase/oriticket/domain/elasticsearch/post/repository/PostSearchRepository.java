package com.zerobase.oriticket.domain.elasticsearch.post.repository;

import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostSearchDocument, Long> {
}
