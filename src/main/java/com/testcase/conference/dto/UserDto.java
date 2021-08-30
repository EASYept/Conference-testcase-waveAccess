package com.testcase.conference.dto;

import com.testcase.conference.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    UUID uuid;
    String userLogin;
    String encrPassword;
    User.Roles role;

}
