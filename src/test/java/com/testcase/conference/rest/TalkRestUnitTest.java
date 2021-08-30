package com.testcase.conference.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.testcase.conference.dto.TalkDto;
import com.testcase.conference.dto.UserDto;
import com.testcase.conference.entity.User;
import com.testcase.conference.security.JwtProvider;
import com.testcase.conference.service.TalkService;
import com.testcase.conference.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(TalkRest.class)
class TalkRestUnitTest {

    @MockBean
    TalkService talkService;
    @MockBean
    UserService userService;
    @MockBean
    JwtProvider jwtProvider;

    ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();


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


    @Disabled //TODO Understand how to set Principal in tests. Because rest endpoint use it on call
    @Test
    void getAll() throws Exception {

        String userName = "testUser";
        String[] speakers = {"Speaker1"};
        when(talkService.findBySpeakers(userName))
                .thenReturn(Collections.singletonList(new TalkDto(UUID.randomUUID(), "Topic", speakers, LocalDateTime.now(), 2)));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/talk/getall"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveTalk_CreatedResponse() throws Exception {
        String[] speakers = {"Speaker1"};
        TalkDto dto = new TalkDto(UUID.randomUUID(), "Topic", speakers, LocalDateTime.now(), 2);
        when(talkService.save(dto))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/talk/save")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    void updateTalk() {
    }

    @Test
    void deleteTalk() {
    }
}