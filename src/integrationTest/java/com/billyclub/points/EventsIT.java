package com.billyclub.points;

import com.billyclub.points.model.Event;
import com.billyclub.points.model.dto.EventModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventsIT {

    @Autowired
    private TestRestTemplate restTemplate;
//    @LocalServerPort
//    int randomServerPort;
    @Test
    public void getEvents_returnsAllEvents() throws Exception {
        ResponseEntity<CollectionModel<EventModel>> response =
                restTemplate.exchange("/api/v1/events", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionModel<EventModel>>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent().size()).isEqualTo(4);
    }


    @Test
    public void getEventById_returnsCorrectRecord() throws Exception {
        ResponseEntity<EventModel> response =
                restTemplate.getForEntity("/api/v1/events/1002", EventModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1002L);
        assertThat(response.getBody().getNumOfTimes()).isEqualTo(4);
    }

    @Test
    public void saveEvent_returnsNewRecord() throws Exception {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.of(6, 30, 0);
        Event pe = new Event(null, ld,lt,7);

        HttpEntity<Event> request = new HttpEntity<>(pe);
        ResponseEntity<EventModel> response = restTemplate.postForEntity("/api/v1/events", request, EventModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getNumOfTimes()).isEqualTo(7);
        assertThat(response.getBody().getEventDate()).isEqualTo(ld);
        assertThat(response.getBody().getStartTime()).isEqualTo(lt);

    }
    @Test
    public void updateEvent_updatesCorrectRecord() throws Exception {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.of(6, 30, 0);
        Event pe = new Event(1000L,ld,lt,5);

        HttpEntity<Event> request = new HttpEntity<>(pe);

        ResponseEntity<EventModel> response = restTemplate.exchange("/api/v1/events/1000", HttpMethod.PUT, request, EventModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1000L);
        assertThat(response.getBody().getNumOfTimes()).isEqualTo(5);
        assertThat(response.getBody().getEventDate()).isEqualTo(ld);
        assertThat(response.getBody().getStartTime()).isEqualTo(lt);
    }
}
