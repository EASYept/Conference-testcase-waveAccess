package com.testcase.conference.service;

import com.testcase.conference.Repository.UserRepository;
import com.testcase.conference.dto.UserDto;
import com.testcase.conference.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void findAll() {
        underTest.findAll();
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void save_MustBeSame_LoginPasswordRole() {
        UserDto dto = new UserDto(UUID.randomUUID(), "login123", "password123", User.Roles.LISTENER);
        when(passwordEncoder.encode(dto.getEncrPassword())).thenReturn("ENCRIPTED?");

        underTest.save(dto);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());

        User capturedPerson = userArgumentCaptor.getValue();
        assertEquals(dto.getUserLogin(), capturedPerson.getUserLogin());
        assertEquals(dto.getRole(), capturedPerson.getRole());
        assertEquals(passwordEncoder.encode(dto.getEncrPassword()), capturedPerson.getEncrPassword());
    }
}