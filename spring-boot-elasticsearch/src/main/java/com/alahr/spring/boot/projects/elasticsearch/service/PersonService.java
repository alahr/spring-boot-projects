package com.alahr.spring.boot.projects.elasticsearch.service;

import com.alahr.spring.boot.projects.elasticsearch.model.Person;
import com.alahr.spring.boot.projects.elasticsearch.model.PersonRequest;
import com.alahr.spring.boot.projects.elasticsearch.model.ResultResponse;

import java.util.Map;

public interface PersonService {
    Map<String, Object> get(String id);

    void add(Person person);

    void update(Person person);

    void delete(String id);

    ResultResponse query(PersonRequest param);
}
