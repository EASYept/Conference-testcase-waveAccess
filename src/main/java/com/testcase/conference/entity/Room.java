package com.testcase.conference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Room extends AbstractEntity {

    @OneToMany(
            mappedBy = "room",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.REFRESH, CascadeType.DETACH})
    List<TalkSchedule> talkList;

    @Column(unique = true)
    int num;

    public Room(int num) {
        this.num = num;
    }

    public void addTalkSchedule(TalkSchedule talkSchedule) {
        talkList.add(talkSchedule);
        talkSchedule.setRoom(this);
    }


}
