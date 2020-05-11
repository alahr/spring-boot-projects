package com.alahr.spring.boot.projects.elasticsearch.service.impl;

import com.alahr.spring.boot.projects.elasticsearch.model.Person;
import com.alahr.spring.boot.projects.elasticsearch.service.PersonService;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService {
    private Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Value("${es.alias}")
    private String esAlias;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Map<String, Object> get(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }

        GetRequest request = new GetRequest(esAlias, id);
        try {
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            return response.getSourceAsMap();
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                logger.error("CONFLICT when get, doc: {}", id, e);
            }
        } catch (Exception e) {
            logger.error("Unknown exception when get, id: {}", id, e);
        }

        return null;
    }

    @Override
    public void add(Person person) {
        if (null == person || StringUtils.isEmpty(person.getId())) {
            return;
        }
        IndexRequest request = new IndexRequest(esAlias);
        request.id(person.getId());
        request.source(JSON.toJSONString(person), XContentType.JSON);

        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                logger.error("CONFLICT when add, doc: {}", person.getId(), e);
            }
        } catch (Exception e) {
            logger.error("Unknown exception when add", e);
        }
    }

    @Override
    public void update(Person person) {
        if (null == person || StringUtils.isEmpty(person.getId())) {
            return;
        }

        UpdateRequest request = new UpdateRequest(esAlias, person.getId());
        request.doc(JSON.toJSONString(person), XContentType.JSON);
        try {
            restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                logger.error("CONFLICT when update, doc: {}", person.getId(), e);
            }
        } catch (Exception e) {
            logger.error("Unknown exception update add", e);
        }
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isEmpty(id)) {
            return;
        }
        DeleteRequest request = new DeleteRequest(esAlias, id);
        try {
            restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.CONFLICT) {
                logger.error("CONFLICT when delete, doc: {}", id, e);
            }
        } catch (Exception e) {
            logger.error("Unknown exception when delete", e);
        }

    }
}
