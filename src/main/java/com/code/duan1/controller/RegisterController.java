package com.code.duan1.controller;

import com.code.duan1.exception.RegisterException;
import com.code.duan1.payload.request.RegisterRequest;
import com.code.duan1.service.authentication.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@AllArgsConstructor
public class RegisterController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register_customer(@RequestBody RegisterRequest UserRegisterRequest){
        try{
            String message = authenticationService.registerUser(UserRegisterRequest);
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (RegisterException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
