package com.billyclub.points.repo;

import com.billyclub.points.model.Event;
import com.billyclub.points.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@SpringBootTest
public class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    public void findAll_returnsAll() throws Exception {
        System.out.println("findAll....");
        List<Event> events = new ArrayList<>();
        for(int i=0;i<3;i++) {
//            long id = i +1000;
            LocalTime lt = LocalTime.of(6,30,0);
            Event pe = new Event(null,LocalDate.now(), lt ,i+1);
            events.add(pe);
//            eventRepository.save(pe);

        }
        eventRepository.saveAll(events);
        List<Event> result = (List<Event>) eventRepository.findAll();

        assertThat(result.size()).isGreaterThanOrEqualTo(3);
        assertThat(result.containsAll(events));
    }

    @Test
    public void addEvent_createsNewAndReturns() throws Exception {

        LocalDate ld = LocalDate.of(2022, Month.APRIL,2);
        LocalTime lt = LocalTime.of(6,30,0);
        Event pe = new Event( null,ld, lt ,6);

        Event result = (Event) eventRepository.save(pe);

        assertThat(result.getEventDate()).isEqualTo(pe.getEventDate());
        assertThat(result.getStartTime()).isEqualTo(pe.getStartTime());
        assertThat(result.getNumOfTimes()).isEqualTo(pe.getNumOfTimes());
    }
//    @Test
//    public void findByDate_returnsCorrect() throws Exception {
//
//        LocalTime lt = LocalTime.of(6,30,0);
//        Event pe = new Event(null, LocalDate.now().plusDays(14), lt ,2);
////        long id = 1000;
//        Optional<List<Event>> result = eventRepository.findByEventDate(LocalDate.now().plusDays(14));
//
//        if(result.isPresent()) {
//            assertThat(result.get().size()).isEqualTo(2);
//
//            //            assertThat(result.get().getEventDate()).isEqualTo(pe.getEventDate());
////            assertThat(result.get().getStartTime()).isEqualTo(pe.getStartTime());
////            assertThat(result.get().getNumOfTimes()).isEqualTo(pe.getNumOfTimes());
//        }else
//            fail("failed to find any results for findByDate");
//    }

    @Test
    public void findById_returnsCorrect() throws Exception {

        LocalTime lt = LocalTime.of(6,30,0);
        Event pe = new Event( null,LocalDate.now().plusDays(8), lt ,3);
        long id = 1001;
        Optional<Event> result = eventRepository.findById(id);

        if(result.isPresent()) {
            assertThat(result.get().getEventDate()).isEqualTo(pe.getEventDate());
            assertThat(result.get().getStartTime()).isEqualTo(pe.getStartTime());
            assertThat(result.get().getNumOfTimes()).isEqualTo(pe.getNumOfTimes());
        }else
            fail("Could not find Event");
    }

    @Test
    public void edit_changesValuesProperly() throws Exception {

        LocalTime lt = LocalTime.of(7,30,0);
        long id = 1000;
        Optional<Event> result = eventRepository.findById(id);

        if(result.isPresent()) {
            Event p = result.get();
            p.setStartTime(lt);
            p.setEventDate(p.getEventDate().plusDays(7));
            p.setNumOfTimes(4);
            eventRepository.save(p);
            Optional<Event> pointsEventOptional = eventRepository.findById(id);
            if(pointsEventOptional.isPresent()){
                assertThat(result.get().getEventDate()).isEqualTo(LocalDate.now().plusDays(14));
                assertThat(result.get().getStartTime()).isEqualTo(LocalTime.of(7,30,0));
                assertThat(result.get().getNumOfTimes()).isEqualTo(4);

            }
        }else
            fail("Could not find Event for edit test");


    }

}
