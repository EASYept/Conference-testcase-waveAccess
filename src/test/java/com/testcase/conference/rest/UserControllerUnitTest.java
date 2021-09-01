package com.testcase.conference.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testcase.conference.dto.UserDto;
import com.testcase.conference.entity.User;
import com.testcase.conference.security.JwtProvider;
import com.testcase.conference.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.status;

@WebMvcTest(UserController.class)
class UserControllerUnitTest {

    @MockBean
    UserService userService;
    @MockBean
    JwtProvider jwtProvider;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach()
    public void setup()
    {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build(); //without it every request will return 403 TODO understand why
    }


    @Test
    void getAll() throws Exception {
        when(userService.findAll())
                .thenReturn(Collections.singletonList(new UserDto(UUID.randomUUID(), "login123", "password123", User.Roles.LISTENER)));
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/getall")
//                .header("Authorization", "Bearer " + "TOKEN") //TODO even without token will return all. Understand how to run it with SpringSecurity
        )
                .andExpect(MockMvcResultMatchers.status().isOk());



    }

    @Test
    void getByUuid_OkResponse() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.findById(uuid))
                .thenReturn(status(HttpStatus.OK).body( new UserDto(uuid, "kekw", "password", User.Roles.LISTENER) ));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/users/get/" + uuid)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userLogin").value("kekw"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uuid").value(uuid.toString()));
    }

    @Test
    void getByUuid_BadRequest() throws Exception {
        UUID uuid = UUID.randomUUID();
        when(userService.findById(uuid))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/get/" + uuid)
        )
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void saveUser_CreatedResponse() throws Exception {
        UserDto dto = new UserDto(UUID.randomUUID(), "login123", "password123", User.Roles.LISTENER);
        dto.setUuid(null);
        when(userService.save(dto))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/save")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

}