package com.testcase.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
Dto for login request or registration(signup) request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOrRegistrationRequest {
    private String userLogin;
    private String password;

}
