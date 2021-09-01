package com.testcase.conference.service;

import com.testcase.conference.entity.Room;
import com.testcase.conference.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> findByNum(int num) {
        return roomRepository.findByNum(num);
    }


}
