package com.alahr.spring.boot.projects.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
public class RestHighLevelClientConfig {
    @Value("${es.hosts}")
    private String hosts;
    @Value("${es.port}")
    private Integer port;
    @Value("${es.scheme}")
    private String scheme;

    @Bean(value = "restHighLevelClient")
    public RestHighLevelClient init(){
        List<HttpHost> list = new ArrayList<>();
        for(String host : hosts.split(",")){
            HttpHost httpHost = new HttpHost(host, port, scheme);
            list.add(httpHost);
        }

        RestClientBuilder builder = RestClient.builder(list.toArray(new HttpHost[0]));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    @PreDestroy
    public void close(){

    }

}
