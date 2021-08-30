package com.testcase.conference.Repository;

import com.testcase.conference.entity.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends CrudRepository<Room, UUID> {

    List<Room> findAll();

    Room findByNum(int num);

}
