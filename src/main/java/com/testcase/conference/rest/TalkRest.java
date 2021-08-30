package com.testcase.conference.rest;

import com.testcase.conference.dto.TalkDto;
import com.testcase.conference.entity.Talk;
import com.testcase.conference.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/talk")
@CrossOrigin(origins = "http://localhost:4200")
public class TalkRest {

    private final TalkService talkService;

    @Autowired
    public TalkRest(TalkService talkService) {
        this.talkService = talkService;
    }

    @GetMapping(path = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<TalkDto> getAll(Principal principal) {
        String userLogin = principal.getName();
        return this.talkService.findBySpeakers(userLogin);
    }

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveTalk(@RequestBody TalkDto talkDto) {
        return this.talkService.save(talkDto);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateTalk(@RequestBody TalkDto talkDto) {
        return this.talkService.update(talkDto);
    }

    @DeleteMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteTalk(@RequestBody TalkDto talkDto) {
        this.talkService.delete(talkDto);
    }


}
