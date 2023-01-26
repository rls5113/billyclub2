package com.billyclub.points.controller;

import com.billyclub.points.model.Player;
import com.billyclub.points.model.assembler.PlayerModelAssembler;
import com.billyclub.points.model.dto.PlayerModel;
import com.billyclub.points.service.impl.PlayerServiceImpl;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PlayerController.class)
@AutoConfigureMockMvc
public class PlayerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PlayerServiceImpl service;
    @MockBean
    private PlayerModelAssembler assembler;
    @Autowired ObjectMapper objectMapper;
    //get by id
    @Test
    public void getById_shouldReturnOneEvent() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Player p = new Player(1000L,"Herman Munster",25,0,now);
        PlayerModel model = new PlayerModel();
        BeanUtils.copyProperties(p,model);
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        when(service.findById(any())).thenReturn(p);
        when(assembler.toModel(any())).thenReturn(model);
        System.out.println(objectMapper.writeValueAsString(p));
        System.out.println(objectMapper.writeValueAsString(model));
        ResultActions result =
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/players/{id}",1000L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(model))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000L))
                .andExpect(jsonPath("$.name").value(p.getName()))
                .andExpect(jsonPath("$.pointsThisEvent").value(p.getPointsThisEvent()))
                .andExpect(jsonPath("$.pointsToPull").value(p.getPointsToPull()))
                .andExpect(jsonPath("$.timeEntered").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        ;
        result.andDo(MockMvcResultHandlers.print());
    }

    //update
    @Test
    public void updatePointsEvent_shouldChangeEventValues() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Player p = new Player(1000L,"Herman Munster",25,27, now);
        Player p1 = new Player(1000L,"Hermie Munster",21,23, now);
        PlayerModel model = new PlayerModel();
        BeanUtils.copyProperties(p1, model);

        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        when(service.findById(any())).thenReturn(p);
        when(service.update(any(), any())).thenReturn(p1);
        when(assembler.toModel(any())).thenReturn(model);

        ResultActions result =
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/players/1000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(model))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1000L))
                .andExpect(jsonPath("$.name").value(p1.getName()))
                .andExpect(jsonPath("$.pointsThisEvent").value(p1.getPointsThisEvent()))
                .andExpect(jsonPath("$.pointsToPull").value(p1.getPointsToPull()))
                .andExpect(jsonPath("$.timeEntered").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        ;
        result.andDo(MockMvcResultHandlers.print());

    }
    //delete
    @Test
    public void deletePointsEvent_shouldRemoveEventRecord() throws Exception {
        Player p = new Player(1000L,"Herman Munster",25,27, LocalDateTime.now());
        PlayerModel model = new PlayerModel();
        BeanUtils.copyProperties(p,model);
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        when(service.findById(any())).thenReturn(p);
        when(service.deleteById(any())).thenReturn(p);
        when(assembler.toModel(any())).thenReturn(model);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/players/1000")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(p))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1000L))
                ;
        result.andDo(MockMvcResultHandlers.print());
    }

    //post
    @Test
    public void addNewPointsEvent_shouldCreateAndReturnEvent() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Player p = new Player(1000L,"Herman Munster",25,27, LocalDateTime.now());
        PlayerModel model = new PlayerModel();
        BeanUtils.copyProperties(p,model);
        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        when(service.findById(any())).thenReturn(p);
        when(service.add(any())).thenReturn(p);
        when(assembler.toModel(any())).thenReturn(model);
        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/players")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(model))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value(1000L))
                        .andExpect(jsonPath("$.name").value(p.getName()))
                        .andExpect(jsonPath("$.pointsThisEvent").value(p.getPointsThisEvent()))
                        .andExpect(jsonPath("$.pointsToPull").value(p.getPointsToPull()))
                        .andExpect(jsonPath("$.timeEntered").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                ;
        result.andDo(MockMvcResultHandlers.print());
    }

    //get all
    @Test
    public void getAllPlayers_shouldReturnAll() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        List<Player> players = new ArrayList<>();
        List<PlayerModel> models = new ArrayList<>();

        objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        String[] names = {"Herman Munster","Lilly Munster","Eddie Munster"};
        Integer[] points = {25,20,15};
        Integer[] actual = {27,18,18};
        for (int i = 0; i < 3; i++) {
            long id = i + 1000;
            Player p = new Player(id,names[i], points[i],actual[i],now);
            players.add(p);
            PlayerModel model = new PlayerModel();
            BeanUtils.copyProperties(p, model);
            models.add(model);
        }

        System.out.println(objectMapper.writeValueAsString(players));
        System.out.println(objectMapper.writeValueAsString(models));

        when(service.findAll()).thenReturn(players);
        when(assembler.toCollectionModel(any())).thenReturn(CollectionModel.of(models));

        ResultActions result =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/players")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(players))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$._embedded.events.size()").value(3))
                        .andExpect(jsonPath("$._embedded.events[0].id").value(1000L))
                        .andExpect(jsonPath("$._embedded.events[0].name").value("Herman Munster"))
                        .andExpect(jsonPath("$._embedded.events[0].pointsThisEvent").value(27))
                        .andExpect(jsonPath("$._embedded.events[0].pointsToPull").value(25))
                        .andExpect(jsonPath("$._embedded.events[0].timeEntered").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                        .andExpect(jsonPath("$._embedded.events[1].id").value(1001L))
                        .andExpect(jsonPath("$._embedded.events[1].name").value("Lilly Munster"))
                        .andExpect(jsonPath("$._embedded.events[1].pointsThisEvent").value(18))
                        .andExpect(jsonPath("$._embedded.events[1].pointsToPull").value(20))
                        .andExpect(jsonPath("$._embedded.events[1].timeEntered").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                        .andExpect(jsonPath("$._embedded.events[2].id").value(1002L))
                        .andExpect(jsonPath("$._embedded.events[2].name").value("Eddie Munster"))
                        .andExpect(jsonPath("$._embedded.events[2].pointsThisEvent").value(18))
                        .andExpect(jsonPath("$._embedded.events[2].pointsToPull").value(15))
                        .andExpect(jsonPath("$._embedded.events[2].timeEntered").value(now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                ;
        result.andDo(MockMvcResultHandlers.print());
    }
}
