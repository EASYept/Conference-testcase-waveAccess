package com.testcase.conference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "talk")
public class Talk extends AbstractEntity {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @Transient
    public static final int TALK_DURATION_HOURS = 2;

    @ManyToMany
    @JoinTable(
            name = "user_talk",
            joinColumns = @JoinColumn(name = "talk_uuid"),
            inverseJoinColumns = @JoinColumn(name = "user_uuid")
    )
    @JsonIgnore
    List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_owned_talks",
            joinColumns = @JoinColumn(name = "talk_uuid"),
            inverseJoinColumns = @JoinColumn(name = "user_uuid")
    )
    @JsonIgnore
    @ToString.Exclude
    List<User> speakers = new ArrayList<>();

    @OneToOne(mappedBy = "talk", cascade = {CascadeType.ALL})
    @ToString.Exclude
    @JsonIgnore
    TalkSchedule talkSchedule;

    String topic;

    public Talk(String topic, User speaker) {
        this.topic = topic;
        addSpeakers(speaker);
    }

    public boolean addSpeakers(User speaker) {
        if (speaker != null) {
            this.speakers.add(speaker);
            return true;
        }
        return false;
    }

    public boolean addListener(User listener) {
        if (listener != null) {
            this.users.add(listener);
            return true;
        }
        return false;
    }


    public void setTalkSchedule(TalkSchedule talkSchedule) {
        if (this.talkSchedule == talkSchedule){
            return;
        }
        this.talkSchedule = talkSchedule;
        talkSchedule.setTalk(this);
    }

//    @PostLoad
//    public void populateSpeakersListeners(){
//        for (User u : this.users) {
//            if (u.role == User.Roles.LISTENER) {
//                listeners.add(u);
//            } else if (u.role == User.Roles.SPEAKER) {
//                speakers.add(u);
//            } else {
//                // do nothing because it's other role (ADMIN)
//            }
//        }
//    }
}
