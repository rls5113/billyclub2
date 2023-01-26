package com.billyclub.points.controller;

import com.billyclub.points.model.Event;
import com.billyclub.points.model.assembler.EventModelAssembler;
import com.billyclub.points.model.assembler.PlayerModelAssembler;
import com.billyclub.points.model.dto.EventModel;
import com.billyclub.points.service.impl.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@AutoConfigureJsonTesters
@WebMvcTest(EventController.class)
@AutoConfigureMockMvc
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventServiceImpl eventService;
    @MockBean
    private EventModelAssembler assembler;
    @MockBean
    private PlayerModelAssembler playerAssembler;
    @Autowired ObjectMapper objectMapper;
    //get by id
    @Test
    public void getById_shouldReturnOneEvent() throws Exception {
        LocalDate ld = LocalDate.now().plusDays(8);
        LocalTime lt = LocalTime.parse("06:30:00",DateTimeFormatter.ofPattern("HH:mm:ss"));       //of(6,30,00);
        Event pe = new Event(1003L, ld, lt, 6);
        EventModel model = new EventModel();
        BeanUtils.copyProperties(pe, model);
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        when(eventService.findById(any())).thenReturn(pe);
        when(assembler.toModel(any())).thenReturn(model);
        System.out.println(objectMapper.writeValueAsString(model));
        ResultActions result =
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events/{id}",1003L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(model))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1003L))
                .andExpect(jsonPath("$.eventDate").value(ld.toString()))
                .andExpect(jsonPath("$.startTime").value("06:30:00"))
                .andExpect(jsonPath("$.numOfTimes").value("6"))
        ;
        result.andDo(MockMvcResultHandlers.print());
    }

    //update
    @Test
    public void updateEvent_shouldChangeEventValues() throws Exception {
        LocalDate ld = LocalDate.of(2022, Month.APRIL,2);
        LocalTime lt = LocalTime.of(6,30,0);
//        LocalDateTime ldt = LocalDateTime.of(2021, Month.APRIL,2,0,0);
        Event pe = new Event(1000L,ld,lt,2);

        LocalDate ld1 = LocalDate.of(2021, Month.APRIL,8);
        LocalTime lt1 = LocalTime.of(6, 30,0);
        Event pe1 = new Event(1000L,ld1,lt1,6);
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        EventModel model = new EventModel();
        BeanUtils.copyProperties(pe1, model);

        when(eventService.findById(any())).thenReturn(pe);
        when(eventService.update(any(), any(Event.class))).thenReturn(pe1);
        when(assembler.toModel(any())).thenReturn(model);

        ResultActions result =
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/events/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pe1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000L))
                .andExpect(jsonPath("$.eventDate").value(ld1.toString()))
                .andExpect(jsonPath("$.startTime").value("06:30:00"))
                .andExpect(jsonPath("$.numOfTimes").value("6"))
        ;
        result.andDo(MockMvcResultHandlers.print());

    }
    //delete
    @Test
    public void deleteEvent_shouldRemoveEventRecord() throws Exception {
        LocalDate ld = LocalDate.of(2022, Month.APRIL,2);
        LocalTime lt = LocalTime.of(6,30,0);
//        LocalDateTime ldt = LocalDateTime.of(2021, Month.APRIL,2,0,0);
        Event pe = new Event(1000L,ld,lt,2);
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        EventModel model = new EventModel();
        BeanUtils.copyProperties(pe, model);

        when(eventService.findById(any())).thenReturn(pe);
        when(eventService.update(any(), any(Event.class))).thenReturn(pe);
        when(assembler.toModel(any())).thenReturn(model);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/events/1000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pe))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1000L))
                        .andExpect(jsonPath("$.eventDate").value(ld.toString()))
                        .andExpect(jsonPath("$.startTime").value("06:30:00"))
                        .andExpect(jsonPath("$.numOfTimes").value(2))
                ;
        result.andDo(MockMvcResultHandlers.print());
    }

    //post
    @Test
    public void addNewEvent_shouldCreateAndReturnEvent() throws Exception {
        LocalDate ld = LocalDate.of(2022, Month.APRIL,2);
        LocalTime lt = LocalTime.of(6,30,0);
//        LocalDateTime ldt = LocalDateTime.of(2021, Month.APRIL,2,0,0);
        Event pe = new Event(1000L,ld,lt,2);
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        EventModel model = new EventModel();
        BeanUtils.copyProperties(pe, model);

        when(eventService.findById(any())).thenReturn(pe);
        when(eventService.add(any(Event.class))).thenReturn(pe);
        when(assembler.toModel(any())).thenReturn(model);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pe))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value(1000L))
                        .andExpect(jsonPath("$.eventDate").value(ld.toString()))
                        .andExpect(jsonPath("$.startTime").value("06:30:00"))
                        .andExpect(jsonPath("$.numOfTimes").value(2))
                ;
        result.andDo(MockMvcResultHandlers.print());
    }

    //get all
    @Test
    public void getAllEvents_shouldReturnAllEvent() throws Exception {
        List<Event> events = new ArrayList<>();
        List<EventModel> models = new ArrayList<>();
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        for (int i = 0; i < 3; i++) {
//            long id = i;
            LocalDate ld = LocalDate.of(2022, Month.APRIL, 2+i);
            LocalTime lt = LocalTime.of(6, 30, 0);
            Event pe = new Event(Long.valueOf(i), ld, lt, i + 1);
            events.add(pe);
            EventModel model = new EventModel();
            BeanUtils.copyProperties(pe,model);
            models.add(model);
        }
//        for (Event event : events) {
//            when(assembler.toModel(event)).thenReturn(assembler.toModel(event));
//        }

        System.out.println(objectMapper.writeValueAsString(events));

        when(eventService.findAll()).thenReturn(events);
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(models));

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(events))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$._embedded.events.size()").value(3))
                        .andExpect(jsonPath("$._embedded.events[0].eventDate").value("2022-04-02"))
                        .andExpect(jsonPath("$._embedded.events[0].startTime").value("06:30:00"))
                        .andExpect(jsonPath("$._embedded.events[0].numOfTimes").value("1"))
                        .andExpect(jsonPath("$._embedded.events[1].eventDate").value("2022-04-03"))
                        .andExpect(jsonPath("$._embedded.events[1].startTime").value("06:30:00"))
                        .andExpect(jsonPath("$._embedded.events[1].numOfTimes").value("2"))
                        .andExpect(jsonPath("$._embedded.events[2].eventDate").value("2022-04-04"))
                        .andExpect(jsonPath("$._embedded.events[2].startTime").value("06:30:00"))
                        .andExpect(jsonPath("$._embedded.events[2].numOfTimes").value("3"))
                ;
        result.andDo(MockMvcResultHandlers.print());
    }
}
