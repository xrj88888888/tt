package com.es.controller;


import com.es.bean.User;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EsController {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/es")
    public User es(){
        System.out.println(Thread.currentThread().getName());
        return new User(1,"aa啊");
    }
}
