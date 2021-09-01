package com.testcase.conference.rest;

import com.testcase.conference.dto.UserDto;
import com.testcase.conference.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getAll() {
        return status(HttpStatus.OK).body(this.userService.findAll());
    }

    @GetMapping(path = "/get/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getByUuid(@PathVariable UUID uuid) {
        return this.userService.findById(uuid);
    }

    //TODO Rep
    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUser(@RequestBody UserDto userDto) {
        return this.userService.save(userDto);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateUser(@RequestBody UserDto userDto) {
        return this.userService.update(userDto);
    }

    @GetMapping(path = "/delete/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        this.userService.deleteById(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
