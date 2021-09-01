package com.testcase.conference.service;

import com.testcase.conference.dto.TalkDto;
import com.testcase.conference.entity.Room;
import com.testcase.conference.entity.Talk;
import com.testcase.conference.entity.User;
import com.testcase.conference.repository.RoomRepository;
import com.testcase.conference.repository.TalkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TalkServiceUnitTest {


    @Mock private TalkRepository talkRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private UserService userService;
    TalkService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TalkService(talkRepository, roomRepository, userService);
    }


    @Test
    void save() {
        Room room = new Room(5);
        User user = new User("Speaker123", "password", User.Roles.SPEAKER);

        String[] speakers = {"Speaker123"};
        TalkDto talkDto = new TalkDto();
        talkDto.setSpeakers(speakers);
        talkDto.setRoomNum(5);
        talkDto.setTopic("Topic");
        talkDto.setDateTime(LocalDateTime.now());

        when(roomRepository.findByNum(talkDto.getRoomNum())).thenReturn(java.util.Optional.of(room));
        when(talkRepository.isThisTimeFree(any(LocalDateTime.class), any(LocalDateTime.class), anyInt())).thenReturn(true);
        when(userService.findByLogin(speakers[0])).thenReturn(java.util.Optional.of(user));

        underTest.save(talkDto);

        ArgumentCaptor<Talk> talkArgumentCaptor = ArgumentCaptor.forClass(Talk.class);
        Mockito.verify(talkRepository).save(talkArgumentCaptor.capture());

        Talk captured = talkArgumentCaptor.getValue();
        assertEquals(talkDto.getTopic(), captured.getTopic());
        assertEquals(talkDto.getRoomNum(), captured.getTalkSchedule().getRoom().getNum());
        assertEquals(talkDto.getDateTime(), captured.getTalkSchedule().getLocalDateTime());
    }


}