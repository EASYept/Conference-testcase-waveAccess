package com.testcase.conference.service;

import com.testcase.conference.dto.LoginOrRegistrationRequest;
import com.testcase.conference.dto.ResponseWithToken;
import com.testcase.conference.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }


    public ResponseWithToken login(LoginOrRegistrationRequest loginOrRegistrationRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(
                        loginOrRegistrationRequest.getUserLogin(), loginOrRegistrationRequest.getPassword());
//        System.out.println("BEFORE AUTHENTICATE");
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//        System.out.println(authenticate);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);


        return new ResponseWithToken(token, loginOrRegistrationRequest.getUserLogin());

    }

}
