package com.billyclub.points.service;

import com.billyclub.points.model.Event;
import com.billyclub.points.repository.EventRepository;
import com.billyclub.points.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventServiceImpl eventService;

//    @Before
//    public void setUp() throws Exception {
//        pointsEventService = new EventService(pointsEventRepository);
//    }


    @Test   //findall
    public void getEventDetails_returnsEventInfo() throws Exception {
        LocalDate ld = LocalDate.of(2022, Month.APRIL,2);
        LocalTime lt = LocalTime.of(6,30,0);

        Event pe = new Event(1000L, ld,lt,3);
        List<Event> list = new ArrayList<>();
        list.add(pe);
        given(eventRepository.findAll()).willReturn(list);
        List<Event> result = eventService.findAll();

        assertThat(result.size()).isEqualTo(Integer.valueOf("1"));

    }

    @Test  //findbyid
    public void getEventById_returnsCorrectEvent() throws Exception {
        LocalDate ld = LocalDate.of(2022, Month.APRIL,2);
        LocalTime lt = LocalTime.of(6,30,0);
        Event pe = new Event(1000L,ld,lt,3);
        given(eventRepository.findById(1000L)).willReturn(java.util.Optional.of(pe));
        Event result = eventService.findById(1000l);

        assertThat(result.getEventDate()).isEqualTo(ld);
        assertThat(result.getStartTime()).isEqualTo(lt);
        assertThat(result.getNumOfTimes()).isEqualTo(Integer.valueOf("3"));
    }

    @Test  //put
    public void putSaves_returnsEventChanges() throws Exception {

        LocalDate ld = LocalDate.of(2022, Month.APRIL,2);
        LocalTime lt = LocalTime.of(6,30,0);
        Event pe = new Event(1000L,ld,lt,3);

        given(eventRepository.findById(1000L)).willReturn(java.util.Optional.of(pe));
        Event result = eventService.findById(1000l);
        result.setNumOfTimes(6);
        given(eventRepository.save(any(Event.class))).willReturn(result);
        Event changed = eventService.update(1000L, result);

        assertThat(changed.getNumOfTimes()).isEqualTo(Integer.valueOf("6"));
    }

//    @Test  //delete
//    public void delete_removesRecord() throws Exception {
//        Event pe = new Event(1, LocalDateTime.of(1962,3,2,0,0),3);
//        given(pointsEventRepository.deleteById(1l)).willReturn();
//        Event result = pointsEventService.getEventById(1l);
//        result.setNumOfTimes(6);
//        given(pointsEventRepository.save(any(Event.class))).willReturn(result);
//        Event changed = pointsEventService.saveEvent(result);
//
//        assertThat(changed.getNumOfTimes()).isEqualTo(Integer.valueOf("6"));
//    }

}
