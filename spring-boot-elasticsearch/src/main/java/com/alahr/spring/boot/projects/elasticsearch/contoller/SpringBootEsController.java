package com.alahr.spring.boot.projects.elasticsearch.contoller;

import com.alahr.spring.boot.projects.elasticsearch.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping(value = "/es")
@RestController
public class SpringBootEsController {
    @Autowired
    private PersonService personService;

    @GetMapping(value = "/")
    public String index() {
        return "Welcome to using SpringBoot and Elasticsearch";
    }

    @GetMapping(value = "/get")
    public Map<String, Object> get(@RequestParam(value = "id") String id) {
        return personService.get(id);
    }
}
