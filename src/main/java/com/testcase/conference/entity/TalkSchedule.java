package com.testcase.conference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "talk_schedule")
public class TalkSchedule extends AbstractEntity {

    LocalDateTime localDateTime;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="room_uuid")
    @ToString.Exclude
    @JsonIgnore
    Room room;


    @OneToOne(cascade = {CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    Talk talk;

    public TalkSchedule(Room room, LocalDateTime localDateTime) {
        this.room = room;
        this.localDateTime = localDateTime;
    }

    public void setRoom(Room room) {
        if (this.room == room)
            return;
        this.room = room;
        room.addTalkSchedule(this);
    }

    public void setTalk(Talk talk) {
        if (this.talk == talk){ //stopping infinite loop
            return;
        }
        this.talk = talk;
        talk.setTalkSchedule(this);
    }

}
