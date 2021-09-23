package com.thodung.shopcaphe.controller;


import java.util.List;

import com.thodung.shopcaphe.model.Post;
import com.thodung.shopcaphe.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<List<Post>>(postService.getAllPosts(),HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>( e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}