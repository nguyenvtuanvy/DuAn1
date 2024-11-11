package com.code.duan1.controller;

import com.code.duan1.exception.RegisterException;
import com.code.duan1.payload.request.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web")
@AllArgsConstructor
public class HomeController {
    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("Hello Word!!!");
    }
}
