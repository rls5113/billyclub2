package com.billyclub.points.model.dto;

import com.billyclub.points.model.Player;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventModel extends RepresentationModel<EventModel>  {
    private Long id;
    private LocalDate eventDate;
    private LocalTime startTime;
    private Integer numOfTimes;
    private List<PlayerModel> players;

}
