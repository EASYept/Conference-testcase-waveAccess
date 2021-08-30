package com.testcase.conference.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "user1")
public class User extends AbstractEntity {

     public enum Roles{
        ADMIN,
        SPEAKER,
        LISTENER
    }



    @Column(unique = true)
    String userLogin;
    String encrPassword;
    Roles role;

    @ManyToMany
    @JoinTable(
            name = "user_talk",
            joinColumns = @JoinColumn(name = "user_uuid"),
            inverseJoinColumns = @JoinColumn(name = "talk_uuid")
    )
    @ToString.Exclude
    @JsonIgnore
    List<Talk> talks;

    @ManyToMany
    @JoinTable(
            name = "user_owned_talks",
            joinColumns = @JoinColumn(name = "user_uuid"),
            inverseJoinColumns = @JoinColumn(name = "talk_uuid")
    )
    @ToString.Exclude
    @JsonIgnore
    List<Talk> ownedTalks;

    public User(String userLogin, String encrPassword, Roles role) {
        this.userLogin = userLogin;
        this.encrPassword = encrPassword;
        this.role = role;
    }


}
