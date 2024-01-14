package com.zerobase.oriticket.domain.elasticsearch.post.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.zerobase.oriticket.domain.elasticsearch.post.entity.PostSearchDocument;
import com.zerobase.oriticket.domain.elasticsearch.post.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.matchPhrase;

@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final PostSearchRepository postSearchRepository;

    private final ElasticsearchTemplate template;

    private final static String CREATED_AT = "createdAt";
    private final Sort sort = Sort.by(CREATED_AT).descending();

    public Page<PostSearchDocument> search(String value, int page, int size) {

        if(value == null){
            return searchAll(page, size);
        }

        return searchByCategoryName(value, page, size);
    }

    private Page<PostSearchDocument> searchAll(int page, int size){

        Pageable pageable = PageRequest.of(page-1, size, sort);

        return postSearchRepository.findAll(pageable);
    }

    private Page<PostSearchDocument> searchByCategoryName(String value, int page, int size){

        Query q = QueryBuilders.bool(builder -> searchPostQuery(builder, value));

        SearchHits<PostSearchDocument> searchHit =
                template.search(NativeQuery.builder()
                                .withQuery(q)
                                .build(),
                        PostSearchDocument.class);

        SearchPage<PostSearchDocument> searchPage =
                SearchHitSupport.searchPageFor(searchHit, PageRequest.of(page-1, size, sort));
        return (Page)SearchHitSupport.unwrapSearchHits(searchPage);
    }

    private BoolQuery.Builder searchPostQuery(BoolQuery.Builder builder, String value){
        builder.should(
                matchPhrase(query -> query.field("sportsName").query(value)),
                matchPhrase(query -> query.field("stadiumName").query(value)),
                matchPhrase(query -> query.field("homeTeamName").query(value)),
                matchPhrase(query -> query.field("awayTeamName").query(value))
        );
        builder.mustNot(
                matchPhrase(query -> query.field("saleStatus").query("REPORTED"))
        );

        return builder;
    }

}
