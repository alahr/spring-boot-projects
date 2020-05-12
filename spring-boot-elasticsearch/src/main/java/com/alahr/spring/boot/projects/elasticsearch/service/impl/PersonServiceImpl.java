package com.alahr.spring.boot.projects.elasticsearch.service.impl;

import com.alahr.spring.boot.projects.elasticsearch.model.Person;
import com.alahr.spring.boot.projects.elasticsearch.model.PersonRequest;
import com.alahr.spring.boot.projects.elasticsearch.model.ResultResponse;
import com.alahr.spring.boot.projects.elasticsearch.service.PersonService;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

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

    public ResultResponse query(PersonRequest param) {
        if (null == param) {
            return new ResultResponse(param.getPageNo(), param.getPageSize(), false, Collections.EMPTY_LIST, "param is null");
        }

        //设置查询参数
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(param.getId())) {
            TermQueryBuilder id = QueryBuilders.termQuery("id", param.getId());
            boolQueryBuilder.must(id);
        }
        if (!StringUtils.isEmpty(param.getName())) {
            TermQueryBuilder name = QueryBuilders.termQuery("name", param.getName());
            boolQueryBuilder.must(name);
        }
        if (null != param.getBirthdayStart() && null != param.getBirthdayEnd()) {
            RangeQueryBuilder birthday = QueryBuilders.rangeQuery("birthday")
                    .gte(param.getBirthdayStart())
                    .lte(param.getBirthdayEnd());
            boolQueryBuilder.must(birthday);
        } else if (null != param.getBirthdayStart() && null == param.getBirthdayEnd()) {
            RangeQueryBuilder birthdayStart = QueryBuilders.rangeQuery("birthday")
                    .gte(param.getBirthdayStart());
            boolQueryBuilder.must(birthdayStart);
        } else if (null == param.getBirthdayStart() && null != param.getBirthdayEnd()) {
            RangeQueryBuilder birthdayEnd = QueryBuilders.rangeQuery("birthday")
                    .lte(param.getBirthdayEnd());
            boolQueryBuilder.must(birthdayEnd);
        }

        Integer fromNum = (param.getPageNo() - 1) * param.getPageSize();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(fromNum);
        searchSourceBuilder.size(param.getPageSize());
        searchSourceBuilder.sort("name", SortOrder.DESC);
        searchSourceBuilder.query(boolQueryBuilder);
        String[] includeFields = new String[]{"name", "birthday", "address"};
        String[] excludeFields = new String[]{"createdTime", "modifiedTime"};
        searchSourceBuilder.fetchSource(includeFields, excludeFields);
        logger.info("query: " + searchSourceBuilder.toString());

        SearchRequest searchRequest = new SearchRequest(esAlias);
        searchRequest.source(searchSourceBuilder);
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                Map<String, Object> map = hit.getSourceAsMap();
                list.add(map);
            }
            return new ResultResponse(param.getPageNo(), param.getPageSize(), true, list, null);
        } catch (ElasticsearchException e) {
            logger.error("ElasticsearchException when query", e);
            return new ResultResponse(param.getPageNo(), param.getPageSize(), false, Collections.EMPTY_LIST, e.getDetailedMessage());
        } catch (Exception e) {
            logger.error("Exception when query", e);
            return new ResultResponse(param.getPageNo(), param.getPageSize(), false, Collections.EMPTY_LIST, e.getMessage());
        }
    }
}
