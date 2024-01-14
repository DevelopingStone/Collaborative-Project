package com.zerobase.oriticket.domain.elasticsearch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elastic.url}")
    private String elasticUrl;

    @Value("${spring.elastic.user}")
    private String elasticUser;

    @Value("${spring.elastic.password}")
    private String elasticPW;


    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo(elasticUrl)
                .withBasicAuth(elasticUser, elasticPW)
                .build();
    }
}
