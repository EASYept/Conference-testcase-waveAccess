package com.testcase.conference.dto;

import lombok.Data;

@Data
public class ResponseWithToken {

    private String authToken;
    private String userLogin;

    public ResponseWithToken() {
    }

    public ResponseWithToken(String token, String userLogin) {
        this.authToken = token;
        this.userLogin = userLogin;
    }
}
