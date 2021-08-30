package com.testcase.conference.Repository;

import com.testcase.conference.entity.Room;
import com.testcase.conference.entity.Talk;
import com.testcase.conference.entity.TalkSchedule;
import com.testcase.conference.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TalkRepository extends CrudRepository<Talk, UUID> {

    List<Talk> findAll();


    @Query(
            nativeQuery = true,
            value = "SELECT TALK.UUID, TOPIC FROM USER_OWNED_TALKS " +
                    "JOIN USER1 " +
                    "ON USER_UUID = USER1.UUID " +
                    "JOIN TALK " +
                    "ON TALK_UUID = TALK.UUID  " +
                    "WHERE USER_LOGIN = ?1"
    )
    List<Talk> findBySpeakers(String userLogin);

    @Query(
            value = "SELECT CAST(CASE WHEN COUNT(*) > 0 THEN 0 ELSE 1 END AS BIT) " +
                    "FROM TALK_SCHEDULE " +
                    "JOIN ROOM " +
                    "ON ROOM_UUID = ROOM.UUID " +
                    "WHERE (LOCAL_DATE_TIME > ?1) " +
                    "AND (LOCAL_DATE_TIME < ?2) " +
                    "AND NUM = ?3",
            nativeQuery = true
    )
    Boolean isThisTimeFree(LocalDateTime before, LocalDateTime after, int roomNum);

}
