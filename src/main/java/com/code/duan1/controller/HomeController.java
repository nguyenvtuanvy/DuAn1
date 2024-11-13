package com.code.duan1.controller;

import com.code.duan1.service.posts.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {
    private PostService postService;

    @GetMapping("/all")
    public ResponseEntity<?> GetAllProduct(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPost());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> GetPostById(@PathVariable("id") Long postId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(postId));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
