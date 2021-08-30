package com.testcase.conference.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOrRegistrationRequest {
    private String userLogin;
    private String password;

}
