package com.testcase.conference.dto;

import com.testcase.conference.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalkDto {

    private UUID uuid;
    private String topic;
    private String[] speakers;
    private LocalDateTime dateTime;
    private int roomNum;


}
