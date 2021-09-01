package com.testcase.conference.service;

import com.testcase.conference.dto.TalkDto;
import com.testcase.conference.entity.Room;
import com.testcase.conference.entity.Talk;
import com.testcase.conference.entity.TalkSchedule;
import com.testcase.conference.entity.User;
import com.testcase.conference.exceptions.MySpringException;
import com.testcase.conference.repository.RoomRepository;
import com.testcase.conference.repository.TalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.status;


//TODO Refactor -> service not supposed to return ResponseEntity. Only entities.
//https://stackoverflow.com/questions/21554977/should-services-always-return-dtos-or-can-they-also-return-domain-models
@Service
@Transactional
public class TalkService {

    private final TalkRepository talkRepository;
    private final RoomRepository roomRepository;
    private final UserService userService;

    @Autowired
    public TalkService(TalkRepository talkRepository, RoomRepository roomRepository, UserService userService) {
        this.talkRepository = talkRepository;
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

    public List<TalkDto> findBySpeakers(String userLogin) {
        /* Initial method */
//        List<Talk> talks = talkRepository.findAllByUserLogin(userLogin);
        /* First way to do same thing */
//        List<Talk> talks = talkRepository.findTalksByUser(userService.findByLogin(userLogin).orElseThrow(() -> new MySpringException("User not found")));
        /* Second way to do same thing  */
        List<Talk> talks = talkRepository.findTalksBySpeakerLogin(userLogin);

        return talks.stream().map(this::mapTalkToDto).collect(Collectors.toList());
    }

    public ResponseEntity<Void> save(TalkDto talkDto) {
        if (talkDto.getRoomNum() > 0
                    && talkDto.getDateTime() != null
                    && !(talkDto.getTopic().isEmpty())
                    && talkDto.getSpeakers() != null) {
            Talk talk = new Talk();
            //persist Talk+ talkSchedule
            talkRepository.save(mapDtoToTalk(talk, talkDto)); //should persist both entity (talk and talkSchedule)

            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public void delete(TalkDto talkDto) {
        talkRepository.deleteById(talkDto.getUuid());
    }

    public ResponseEntity<Void> update(TalkDto talkDto) {
        Optional<Talk> byId = talkRepository.findById(talkDto.getUuid());
        if (byId.isPresent()){
            talkRepository.save(mapDtoToTalk(byId.get(), talkDto));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    public List<TalkDto> findByListener(String userLogin) {
        List<Talk> talks = talkRepository.findByListener(userLogin);

        return talks.stream().map(this::mapTalkToDto).collect(Collectors.toList());
    }

    public void saveTalk(Talk talk) {
        talkRepository.save(talk);
    }

    public Optional<Talk> findById(UUID talkUuid) {
        return talkRepository.findById(talkUuid);
    }

    public ResponseEntity<String> registerOnTalk(UUID talkUuid, Principal principal) {
        Optional<Talk> talkById = this.findById(talkUuid);
        Optional<User> userByLogin = userService.findByLogin(principal.getName());

        //TODO refactor without try/catch
        try {
            talkById.orElseThrow(() ->  new MySpringException("Talk not found"))
                    .addListener(userByLogin
                            .orElseThrow(() -> new MySpringException("User not found")));

            this.saveTalk(talkById.get());
            return status(HttpStatus.OK).body("Successfully registered on talk " + talkById.get().getTopic());
        } catch (MySpringException e) {
            return status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /* Helpers */

    private boolean isThisTimeFree(LocalDateTime localDateTime, int roomNum){
        LocalDateTime after = localDateTime.plusHours(Talk.TALK_DURATION_HOURS);
        LocalDateTime before = localDateTime.minusHours(Talk.TALK_DURATION_HOURS);
        return talkRepository.isThisTimeFree(before, after, roomNum);
    }

    //TODO poorly designed(i'm not handling exception anywhere, so server throw 500 error). Refactor this
    //Mapping TalkDto -> Talk with TalkSchedule
    private Talk mapDtoToTalk(Talk t, TalkDto talkDto) throws MySpringException {
        Room room = roomRepository.findByNum(talkDto.getRoomNum())
                .orElseThrow(() -> new MySpringException("Bad request -> room: not exist"));
        if ((room == null)
                || (!isThisTimeFree(talkDto.getDateTime(), talkDto.getRoomNum()))) {
            throw new MySpringException("Bad request -> " +
                    " room: " + room +
                    " isTimeFree: " + isThisTimeFree(talkDto.getDateTime(), talkDto.getRoomNum()));
        }

        List<User> speakerList = new ArrayList<>(2); //usually it's not more then 2 speakers per talk
        for (String login : talkDto.getSpeakers()) {
            User user = userService.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (user.getRole() == User.Roles.SPEAKER){
                speakerList.add(user);
            }
        }

        if (t.getTalkSchedule() != null) {
            t.getTalkSchedule().setLocalDateTime(talkDto.getDateTime());
            t.getTalkSchedule().setRoom(room);
        } else {
            TalkSchedule talkSchedule = new TalkSchedule(room, talkDto.getDateTime());
            t.setTalkSchedule(talkSchedule);
        }

        t.setSpeakers(speakerList);
        t.setTopic(talkDto.getTopic());

        return t;
    }

    private TalkDto mapTalkToDto(Talk talk){
        TalkDto talkDto = new TalkDto();
        talkDto.setTopic(talk.getTopic());
        talkDto.setDateTime(talk.getTalkSchedule().getLocalDateTime());
        talkDto.setRoomNum(talk.getTalkSchedule().getRoom().getNum());
        talkDto.setUuid(talk.getUuid());
        talkDto.setSpeakers(talk.getSpeakers().stream()
                                                .map(User::getUserLogin)
                                                .toArray(String[]::new));
        return talkDto;
    }

}
