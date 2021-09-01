package com.testcase.conference.rest;

import com.testcase.conference.dto.LoginOrRegistrationRequest;
import com.testcase.conference.dto.ResponseWithToken;
import com.testcase.conference.entity.User;
import com.testcase.conference.repository.UserRepository;
import com.testcase.conference.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/auth")
public class SignupLoginController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @Autowired
    public SignupLoginController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }


    @PostMapping("/signup")
    public String signup(@RequestBody LoginOrRegistrationRequest loginOrRegistrationRequest) {
        User user = new User(loginOrRegistrationRequest.getUserLogin(),
                passwordEncoder.encode(loginOrRegistrationRequest.getPassword()),
                User.Roles.LISTENER);
        userRepository.save(user);
        return "Account created successfully";
    }


    @PostMapping("/login")
    public ResponseWithToken login(@RequestBody LoginOrRegistrationRequest request) {
        return authService.login(request);
    }


}
