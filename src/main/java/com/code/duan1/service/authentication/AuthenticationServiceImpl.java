package com.code.duan1.service.authentication;

import com.code.duan1.entity.User;
import com.code.duan1.exception.LoginException;
import com.code.duan1.exception.RefreshTokenException;
import com.code.duan1.exception.RegisterException;
import com.code.duan1.payload.request.LoginRequest;
import com.code.duan1.payload.request.RefreshTokenRequest;
import com.code.duan1.payload.request.RegisterRequest;
import com.code.duan1.payload.response.AuthenticationResponse;
import com.code.duan1.repository.AdminRepository;
import com.code.duan1.repository.UserRepository;
import com.code.duan1.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public String registerUser(RegisterRequest request) throws RegisterException {
        try{
            if (request.getUserName() != null){
                var user = userRepository.findUserByUsername(request.getUserName());
                if (user.isPresent()){
                    throw new RegisterException("username đã được sử dụng");
                }
            }

            User user = User.builder()
                    .username(request.getUserName())
                    .fullName(request.getFullName())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role("USER")
                    .build();

            userRepository.save(user);

            return "Đăng ký thành công";
        } catch (Exception e){
            e.printStackTrace();
            throw new RegisterException("Đăng ký thất bại");
        }
    }

    @Override
    public AuthenticationResponse authenticate(LoginRequest request) throws LoginException {
        String token = null;
        String refreshToken = null;

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword())
        );

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            UserDetails user= (UserDetails) authentication.getPrincipal();
            token= jwtService.generateAccessToken(user);
            refreshToken= jwtService.generateRefreshToken(user);
        }

        if(token==null && refreshToken==null){
            throw new LoginException("Tài khoản hoặc mật khẩu không đúng!");
        }

        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws RefreshTokenException {
        String newToken=null;
        String refreshToken= request.getRefreshToken();

        if(request.getRefreshToken() !=null){
            if(!jwtService.isTokenExpired(refreshToken)){
                Claims claims= jwtService.decodedToken(refreshToken);
                UserDetails user= this.userDetailsService.loadUserByUsername(claims.getSubject());
                newToken= jwtService.generateTokenByRefreshToken(user);
            }else{
                throw new RefreshTokenException("refreshToken hết hạn");
            }
        } else {
            throw new RefreshTokenException("RefreshToken không có giá trị");
        }
        if(newToken!=null){
            return AuthenticationResponse.builder()
                    .token(newToken)
                    .refreshToken(refreshToken)
                    .build();
        }else{
            throw new RefreshTokenException("refreshToken hết hạn");
        }
    }
}
