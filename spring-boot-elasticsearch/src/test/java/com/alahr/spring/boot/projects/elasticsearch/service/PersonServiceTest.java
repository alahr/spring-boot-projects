package com.alahr.spring.boot.projects.elasticsearch.service;

import com.alahr.spring.boot.projects.common.utils.DateUtil;
import com.alahr.spring.boot.projects.elasticsearch.SpringBootEsMain;
import com.alahr.spring.boot.projects.elasticsearch.model.Person;
import com.alahr.spring.boot.projects.elasticsearch.model.PersonRequest;
import com.alahr.spring.boot.projects.elasticsearch.model.ResultResponse;
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
    public void get() {
        Map<String, Object> map = personService.get("1");
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void add() {
        Person person = new Person();
        person.setId("3");
        person.setName("Zhangfei");
        person.setBirthday("1998-12-12");
        person.setAddress("河北石家庄");
        Date now = new Date();
        person.setCreatedTime(DateUtil.parseDate2String(now));
        person.setModifiedTime(DateUtil.parseDate2String(now));
        personService.add(person);
        Person person2 = new Person();
        person2.setId("4");
        person2.setName("Guanyu");
        person2.setBirthday("1995-08-25");
        person2.setAddress("四川成都");
        person2.setCreatedTime(DateUtil.parseDate2String(now));
        person2.setModifiedTime(DateUtil.parseDate2String(now));
        personService.add(person2);
    }

    @Test
    public void update() {
        Person person = new Person();
        person.setId("1");
        person.setName("Tom");
        person.setBirthday("1995-08-12");
        person.setAddress("Qingpu,Shanghai");
        Date now = new Date();
        person.setModifiedTime(DateUtil.parseDate2String(now));
        personService.update(person);
    }

    @Test
    public void delete() {
        personService.delete("1");
    }

    @Test
    public void query() {
        PersonRequest request = new PersonRequest();
//        request.setId("1");
//        request.setName("Tom");
        request.setBirthdayStart("1990-01-01");
        request.setBirthdayEnd("1994-12-31");
        ResultResponse query = personService.query(request);
        System.out.println(JSON.toJSONString(query));
    }
}
