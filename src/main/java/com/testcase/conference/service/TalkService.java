package com.testcase.conference.service;

import com.testcase.conference.Repository.RoomRepository;
import com.testcase.conference.Repository.TalkRepository;
import com.testcase.conference.dto.TalkDto;
import com.testcase.conference.entity.Room;
import com.testcase.conference.entity.Talk;
import com.testcase.conference.entity.TalkSchedule;
import com.testcase.conference.entity.User;
import com.testcase.conference.exceptions.MySpringException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        List<Talk> talks = talkRepository.findBySpeakers(userLogin);
        List<TalkDto> talkDtoList = new ArrayList<>();
        for (Talk t: talks) {
            TalkDto tDto = new TalkDto();

            List<String> speakersLogins = new ArrayList<>();
            t.getSpeakers().forEach(a -> speakersLogins.add(a.getUserLogin()));
            String[] array = speakersLogins.toArray(new String[0]);
            speakersLogins.toArray(array);

            tDto.setSpeakers(array);
            tDto.setRoomNum(t.getTalkSchedule().getRoom().getNum());
            tDto.setTopic(t.getTopic());
            tDto.setDateTime(t.getTalkSchedule().getLocalDateTime());
            talkDtoList.add(tDto);
            tDto.setUuid(t.getUuid());
        }
        return talkDtoList;
    }

    public ResponseEntity<Void> save(TalkDto talkDto) {
        if (talkDto.getRoomNum() > 0
                    && talkDto.getDateTime() != null
                    && !(talkDto.getTopic().isEmpty())
                    && talkDto.getSpeakers() != null) {
            Talk talk = new Talk();
            //persist Talk+ talkSchedule
            talkRepository.save(map(talk, talkDto)); //should persist both entity (talk and talkSchedule)

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
            talkRepository.save(map(byId.get(), talkDto));
            return new ResponseEntity<Void>(HttpStatus.OK);
        } else {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
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
    private Talk map(Talk t, TalkDto talkDto) throws MySpringException {
        Room room = roomRepository.findByNum(talkDto.getRoomNum());
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

}
