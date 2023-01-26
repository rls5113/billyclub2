package com.billyclub.points.service;

import com.billyclub.points.model.Player;
import com.billyclub.points.repository.PlayerRepository;
import com.billyclub.points.service.impl.PlayerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerRepository repo;
    @InjectMocks
    private PlayerServiceImpl service;


    @Test   //findall
    public void getAll_returnsAll() throws Exception {

//        PointsEvent pe = new PointsEvent(ld,lt,3);
        Player p = new Player(1000L,"Herman Munster", 25, 27, LocalDateTime.now());
        List<Player> list = new ArrayList<>();
        list.add(p);
        given(repo.findAll()).willReturn(list);
        List<Player> result = service.findAll();

        assertThat(result.size()).isEqualTo(Integer.valueOf("1"));

    }

    @Test  //findbyid
    public void getById_returnsCorrect() throws Exception {
        Player p = new Player(1000L,"Herman Munster",25,27,LocalDateTime.now());
        given(repo.findById(1000L)).willReturn(java.util.Optional.of(p));
        Player result = service.findById(1000L);

        assertThat(result.getName()).isEqualTo(p.getName());
        assertThat(result.getPointsToPull()).isEqualTo(p.getPointsToPull());
        assertThat(result.getPointsThisEvent()).isEqualTo(p.getPointsThisEvent());
        assertThat(result.getTimeEntered()).isBeforeOrEqualTo(p.getTimeEntered());
    }

    @Test  //put
    public void putSaves_returnsEventChanges() throws Exception {

        Player p = new Player(1000L,"Herman Munster",25,27,LocalDateTime.now());
        given(repo.findById(1000L)).willReturn(java.util.Optional.of(p));
        Player result = service.findById(1000L);
        result.setName("Hermie Munster");
        result.setPointsToPull(27);
        result.setPointsThisEvent(31);
        given(repo.save(any(Player.class))).willReturn(result);
        Player changed = service.update(1000L, result);

        assertThat(changed.getName()).isEqualTo("Hermie Munster");
        assertThat(changed.getPointsToPull()).isEqualTo(27);
        assertThat(changed.getPointsThisEvent()).isEqualTo(31);
        assertThat(changed.getTimeEntered()).isBeforeOrEqualTo(p.getTimeEntered());
    }

//    @Test  //delete
//    public void delete_removesRecord() throws Exception {
//        PointsEvent pe = new PointsEvent(1, LocalDateTime.of(1962,3,2,0,0),3);
//        given(repo.deleteById(1l)).willReturn();
//        PointsEvent result = service.getPointsEventById(1l);
//        result.setNumOfTimes(6);
//        given(repo.save(any(PointsEvent.class))).willReturn(result);
//        PointsEvent changed = service.savePointsEvent(result);
//
//        assertThat(changed.getNumOfTimes()).isEqualTo(Integer.valueOf("6"));
//    }

}
