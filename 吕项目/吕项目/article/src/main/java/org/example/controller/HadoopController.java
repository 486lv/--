package org.example.controller;


import org.example.entity.Article;
import org.example.entity.Result;
import org.example.service.ArticleService;

import org.example.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.example.service.HadoopService;
@RestController
@RequestMapping("/hadoop")
public class HadoopController {
    @Autowired
    HadoopService hadoopService;
    @GetMapping
    public Result dowork() throws Exception {

        int x=hadoopService.dowork();
        Map<String, Integer> cs = hadoopService.show(x);
        return Result.success(cs);
    }

   /* @GetMapping("/show")
    public Result<Map<String, Integer>> show() throws Exception {

        //articleService.readFileToMap("D:/result/1.txt");
        Map<String, Integer> cs = articleService.show();
        return Result.success(cs);
    }*/






}
