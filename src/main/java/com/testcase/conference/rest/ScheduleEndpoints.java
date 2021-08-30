package com.testcase.conference.rest;

import com.testcase.conference.Repository.RoomRepository;
import com.testcase.conference.Repository.TalkRepository;
import com.testcase.conference.Repository.UserRepository;
import com.testcase.conference.entity.Room;
import com.testcase.conference.entity.Talk;
import com.testcase.conference.entity.User;
import com.testcase.conference.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.status;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ScheduleEndpoints {

    private final RoomRepository roomRepository;
    private final TalkRepository talkRepository;
    private final UserRepository userRepository;


    @Autowired
    public ScheduleEndpoints(RoomRepository roomRepository, TalkRepository talkRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.talkRepository = talkRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @GetMapping(path = "/regOnTalk/{talkUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerOnTalk(@PathVariable UUID talkUuid, Principal principal) {
        Optional<Talk> byId = talkRepository.findById(talkUuid);
        Optional<User> byLogin = userRepository.findByUserLogin(principal.getName());

        if (byId.isPresent() && byLogin.isPresent()) {
            Talk talk = byId.get();
            if (!talk.getUsers().contains(byLogin.get())) {
                talk.addListener(byLogin.get());
            }
            talkRepository.save(talk);

            return status(HttpStatus.OK).body("successfully registered on talk " + talk.getTopic());
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }




}
