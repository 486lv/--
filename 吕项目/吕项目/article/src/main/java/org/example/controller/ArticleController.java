package org.example.controller;

import org.example.entity.Article;
import org.example.entity.PageBean;
import org.example.entity.Result;
import org.example.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody  Article article) throws Exception {
        articleService.add(article);
        //articleService.writeContentToFiles();
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(

            Integer pageNum,

            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state
    ) {
       PageBean<Article> pb =  articleService.list(pageNum,pageSize,categoryId,state);
       return Result.success(pb);
    }

    @PutMapping
    public Result update(@RequestBody Article article) {
        Article a = articleService.update(article);
        return Result.success(a);
    }
    @DeleteMapping
    public Result delete(Integer id) throws Exception {
        articleService.deleteById(id);
        return Result.success();
    }


//
//    @PostMapping("/txt")
//    public void createFilesFromContent() {
//        articleService.writeContentToFiles();
//        System.out.println("1");
//    }
}
