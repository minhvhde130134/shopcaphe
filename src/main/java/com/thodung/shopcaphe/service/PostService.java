package com.thodung.shopcaphe.service;

import java.util.List;

import com.thodung.shopcaphe.model.Post;
import com.thodung.shopcaphe.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}