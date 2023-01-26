package com.billyclub.points;

import com.billyclub.points.model.Player;
import com.billyclub.points.model.dto.PlayerModel;
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
public class PlayersIT {

    @Autowired
    private TestRestTemplate restTemplate;
//    @LocalServerPort
//    int randomServerPort;
    @Test
    public void getPlayers_returnsAllPlayers() throws Exception {
        ResponseEntity<CollectionModel<PlayerModel>> response =
                restTemplate.exchange("/api/v1/players", HttpMethod.GET, null, new ParameterizedTypeReference<CollectionModel<PlayerModel>>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent().size()).isEqualTo(3);
    }


    @Test
    public void getPlayerById_returnsCorrectRecord() throws Exception {
        ResponseEntity<PlayerModel> response =
                restTemplate.getForEntity("/api/v1/players/1002",PlayerModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1002L);
        assertThat(response.getBody().getName()).isEqualTo("Eddie Munster");
        assertThat(response.getBody().getPointsToPull()).isEqualTo(15);
        assertThat(response.getBody().getPointsThisEvent()).isEqualTo(0);
    }

    @Test
    public void savePlayer_returnsNewRecord() throws Exception {
        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.of(6, 30, 0);
        Player pe = new Player("Grandpa Munster",30,0);

        HttpEntity<Player> request = new HttpEntity<>(pe);
        ResponseEntity<PlayerModel> response = restTemplate.postForEntity("/api/v1/players", request, PlayerModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Grandpa Munster");
        assertThat(response.getBody().getPointsToPull()).isEqualTo(30);
        assertThat(response.getBody().getPointsThisEvent()).isEqualTo(0);

    }
    @Test
    public void updatePlayer_updatesCorrectRecord() throws Exception {
        Player pe = new Player(1000L,"Hermie Munster",15,12,null);

        HttpEntity<Player> request = new HttpEntity<>(pe);

        ResponseEntity<PlayerModel> response = restTemplate.exchange("/api/v1/players/1000", HttpMethod.PUT, request, PlayerModel.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(1000L);
        assertThat(response.getBody().getName()).isEqualTo("Hermie Munster");
        assertThat(response.getBody().getPointsToPull()).isEqualTo(15);
        assertThat(response.getBody().getPointsThisEvent()).isEqualTo(12);
        assertThat(response.getBody().getTimeEntered()).isNotNull();
    }
}
