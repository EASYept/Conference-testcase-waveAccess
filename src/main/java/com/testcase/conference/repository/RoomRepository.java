package com.testcase.conference.repository;

import com.testcase.conference.entity.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends CrudRepository<Room, UUID> {

    List<Room> findAll();

    Optional<Room> findByNum(int num);

}
