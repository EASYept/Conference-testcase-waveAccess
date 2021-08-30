package com.testcase.conference.rest;

import com.testcase.conference.Repository.UserRepository;
import com.testcase.conference.dto.LoginOrRegistrationRequest;
import com.testcase.conference.dto.ResponseWithToken;
import com.testcase.conference.entity.User;
import com.testcase.conference.service.AuthService;
import com.testcase.conference.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class SignupLoginEndpoint {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @Autowired
    public SignupLoginEndpoint(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
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


//    @Transactional(readOnly = true)
//    public User getCurrentUser() {
//        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
//                getContext().getAuthentication().getPrincipal();
//        return userService.findByLogin(principal.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
//    }





}
