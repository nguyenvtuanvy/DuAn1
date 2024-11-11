package com.code.duan1.controller;

import com.code.duan1.exception.LoginException;
import com.code.duan1.exception.RegisterException;
import com.code.duan1.payload.request.LoginRequest;
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
public class LoginController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest UserLoginRequest){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(authenticationService.authenticate(UserLoginRequest));
        } catch (LoginException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
