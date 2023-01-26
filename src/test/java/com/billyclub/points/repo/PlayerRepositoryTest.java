package com.billyclub.points.repo;

import com.billyclub.points.model.Player;
import com.billyclub.points.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@SpringBootTest
public class PlayerRepositoryTest {

    @Autowired
    PlayerRepository repo;

    @Test
    public void findAll_returnsAll() throws Exception {
        System.out.println("findAll....");
        String[] names = {"Herman Munster","Lilly Munster","Eddie Munster"};
        Integer[] current = {30,20,15};
        Integer[] pulled = {27,18,18};
        List<Player> players = new ArrayList<>();
        for(int i=0;i<3;i++) {
            long id = i +1000;
            LocalTime lt = LocalTime.of(6,30,0);
            Player player = new Player(id,names[i], current[i],pulled[i], LocalDateTime.now());
            players.add(player);

        }
//        repo.saveAll(players);
        List<Player> result = (List<Player>) repo.findAll();

        assertThat(result.size()).isGreaterThanOrEqualTo(3);
        assertThat(result.containsAll(players));
    }

    @Test
    public void addPointsEvent_createsNewAndReturns() throws Exception {

        Player player = new Player( "Grandpa Munster", 25,27);

        Player result = repo.save(player);

        assertThat(result.getName()).isEqualTo(player.getName());
        assertThat(result.getPointsToPull()).isEqualTo(player.getPointsToPull());
        assertThat(result.getPointsThisEvent()).isEqualTo(player.getPointsThisEvent());
        assertThat(result.getTimeEntered()).isEqualTo(player.getTimeEntered());
    }
    @Test
    public void findById_returnsCorrect() throws Exception {

        Player player = new Player( "Eddie Munster", 15,0);
        long id = 1002;
//        repo.save(player);
        Optional<Player> result = repo.findById(id);

        if(result.isPresent()) {
            assertThat(result.get().getName()).isEqualTo(player.getName());
            assertThat(result.get().getPointsToPull()).isEqualTo(player.getPointsToPull());
            assertThat(result.get().getPointsThisEvent()).isEqualTo(player.getPointsThisEvent());
            assertThat(result.get().getTimeEntered()).isBefore(player.getTimeEntered());
        }else
            fail("Could not find Event");
    }

    @Test
    public void edit_changesValuesProperly() throws Exception {

        Player player = new Player( "Herman Munster", 25,27);
        long id = 1000;
//        player.setId(id);
//        repo.save(player);
        Optional<Player> result = repo.findById(id);

        if(result.isPresent()) {
            Player p = result.get();
            p.setName("Hermie Munster");
            p.setPointsToPull(15);
            p.setPointsThisEvent(12);
            repo.save(p);
            Optional<Player> playerOptional = repo.findById(id);
            if(playerOptional.isPresent()){
                assertThat(result.get().getName()).isEqualTo("Hermie Munster");
                assertThat(result.get().getPointsToPull()).isEqualTo(15);
                assertThat(result.get().getPointsThisEvent()).isEqualTo(12);

            }
        }else
            fail("Could not find Player for edit test");


    }

}
