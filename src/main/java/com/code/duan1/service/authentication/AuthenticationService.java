package com.code.duan1.service.authentication;

import com.code.duan1.exception.LoginException;
import com.code.duan1.exception.RefreshTokenException;
import com.code.duan1.exception.RegisterException;
import com.code.duan1.payload.request.LoginRequest;
import com.code.duan1.payload.request.RefreshTokenRequest;
import com.code.duan1.payload.request.RegisterRequest;
import com.code.duan1.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    String registerUser(RegisterRequest request) throws RegisterException;

    AuthenticationResponse authenticate(LoginRequest request) throws LoginException;

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws RefreshTokenException;
}
