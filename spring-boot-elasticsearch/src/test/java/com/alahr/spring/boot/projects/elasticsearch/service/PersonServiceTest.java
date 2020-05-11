package com.alahr.spring.boot.projects.elasticsearch.service;

import com.alahr.spring.boot.projects.common.utils.DateUtil;
import com.alahr.spring.boot.projects.elasticsearch.SpringBootEsMain;
import com.alahr.spring.boot.projects.elasticsearch.model.Person;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootEsMain.class)
@WebAppConfiguration
public class PersonServiceTest {
    @Autowired
    private PersonService personService;

    @Test
    public void get(){
        Map<String, Object> map = personService.get("2");
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void add(){
        Person person = new Person();
        person.setId("1");
        person.setName("Lucy");
        person.setBirthday(DateUtil.parse("1995-08-12"));
        person.setAddress("Haidian,Beijing");
        Date now = new Date();
        person.setCreatedTime(now);
        person.setModifiedTime(now);
        personService.add(person);
    }

    @Test
    public void update(){
        Person person = new Person();
        person.setId("1");
        person.setName("Tom");
        person.setBirthday(DateUtil.parse("1990-12-25"));
        person.setAddress("Qingpu,Shanghai");
        Date now = new Date();
        person.setModifiedTime(now);
        personService.update(person);
    }

    @Test
    public void delete(){
        personService.delete("1");
    }
}
