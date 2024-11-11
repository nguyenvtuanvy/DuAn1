package com.code.duan1.controller;

import com.code.duan1.exception.RegisterException;
import com.code.duan1.payload.request.RegisterRequest;
import com.code.duan1.service.posts.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web")
@AllArgsConstructor
public class HomeController {
    private PostService postService;

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("Hello Word!!!");
    }

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
