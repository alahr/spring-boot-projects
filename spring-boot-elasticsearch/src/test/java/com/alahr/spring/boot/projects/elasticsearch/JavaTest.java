package com.alahr.spring.boot.projects.elasticsearch;

import com.alahr.spring.boot.projects.elasticsearch.model.Person;

import java.util.Arrays;
import java.util.List;

public class JavaTest {
    public static void main(String[] args) {
        Person p1 = new Person();
        p1.setName("Tom");

        Person p2 = new Person();
        p2.setName("Jim");

        List<Person> list = Arrays.asList(p1, p2);

        Person[] array = list.toArray(new Person[0]);

        for(Person p : array){
            System.out.println(p.getName());
        }
    }
}
