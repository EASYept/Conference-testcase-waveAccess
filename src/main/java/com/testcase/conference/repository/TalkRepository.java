package com.testcase.conference.repository;

import com.testcase.conference.entity.Talk;
import com.testcase.conference.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//Good source https://en.wikibooks.org/wiki/Java_Persistence/JPQL
public interface TalkRepository extends CrudRepository<Talk, UUID> {

    /* HOMEWORK for Irina Belger */
    // create 2 another ways to write initial query

    //Initial Query
    @Query(
        nativeQuery = true,
        value = "SELECT TALK.UUID, TOPIC FROM USER_OWNED_TALKS " +
                "JOIN USER1 " +
                "ON USER_UUID = USER1.UUID " +
                "JOIN TALK " +
                "ON TALK_UUID = TALK.UUID  " +
                "WHERE USER_LOGIN = ?1"
    )
    List<Talk> findAllBySpeakerLogin(String userLogin);

    //FIRST WAY TO DO THIS
    List<Talk> findAllBySpeakersIs(User speaker);

    // SECOND WAY TO DO THIS
    @Query(
            value = "select t from Talk t " +
                    "join t.speakers speakers " +
                    "where speakers.userLogin = :userLogin "
    )
    List<Talk> findTalksBySpeakerLogin(String userLogin);
    /* END OF HOMEWORK */


    List<Talk> findAll();

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

    @Query(
            value = "select t from Talk t " +
                    "join t.users listeners " +
                    "where listeners.userLogin = :userLogin "
    )
    List<Talk> findByListener(String userLogin);

}
