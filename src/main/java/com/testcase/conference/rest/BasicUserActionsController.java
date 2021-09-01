package com.testcase.conference.rest;

import com.testcase.conference.dto.TalkDto;
import com.testcase.conference.entity.Room;
import com.testcase.conference.service.RoomService;
import com.testcase.conference.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
public class BasicUserActionsController {

    private final TalkService talkService;
    private final RoomService roomService;


    @Autowired
    public BasicUserActionsController(TalkService talkService, RoomService roomService) {
        this.talkService = talkService;
        this.roomService = roomService;
    }

    @GetMapping(path = "/rooms", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Room> getAllRooms() {
        return roomService.findAll();
    }

    //TODO GET should not change anything in system.
    @GetMapping(path = "/regOnTalk/{talkUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerOnTalk(@PathVariable UUID talkUuid, Principal principal) {
        return talkService.registerOnTalk(talkUuid, principal);
    }

    //todo rename path
    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<TalkDto> getByListener(Principal principal) {
        String userLogin = principal.getName();
        return this.talkService.findByListener(userLogin);
    }




}
