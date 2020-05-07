package com.robot.bowlingscore.controllers;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BowlingScoreControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getIndex() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome to the Bowling Score!!!\n")));
    }

    @Test
    public void initializeGame() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"players\":[]}"));
    }

    @Test
    public void postNamesToGame() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        JSONArray json = new JSONArray("[rodrigo,john]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "{\"id\":1,\"players\":[{\"name\":\"rodrigo\",\"frames\":[],\"game\":1},{\"name\":\"john\",\"frames\":[],\"game\":1}]}"
                        ));
    }

    @Test
    public void getGameInformation() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        mvc.perform(MockMvcRequestBuilders.get("/game/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"players\":[]}"));
    }

    @Test
    public void getAddPlayerToGame() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        JSONArray json = new JSONArray("[rodrigo,john]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "{\"id\":1,\"players\":[{\"name\":\"rodrigo\",\"frames\":[],\"game\":1},{\"name\":\"john\",\"frames\":[],\"game\":1}]}"
                        ));
    }

    @Test
    public void getAddPlayerToGameAfterStarted() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        JSONArray json = new JSONArray("[rodrigo,john]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()));

        json = new JSONArray("[test]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                "{\"id\":1,\"players\":[{\"name\":\"rodrigo\",\"frames\":[],\"game\":1},{\"name\":\"john\",\"frames\":[],\"game\":1},{\"name\":\"test\",\"frames\":[],\"game\":1}]}"
                        ));
    }
    @Test
    public void addRollToPlayerByGame() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        JSONArray json = new JSONArray("[rodrigo]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()));

        mvc.perform(MockMvcRequestBuilders.put("/game/1/player/rodrigo/roll/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"name\":\"rodrigo\",\"frames\":[{\"id\":1,\"player\":\"rodrigo\",\"firstRoll\":4,\"secondRoll\":-1,\"score\":4,\"rolls\":1,\"spare\":false,\"strike\":false,\"completeFrame\":false,\"bonusBalls\":false,\"bonusBall\":-1}],\"game\":{\"id\":1,\"players\":[\"rodrigo\"]}}"));
    }



        @Test
    public void testGetTotalScoreEmptyFrames() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        JSONArray json = new JSONArray("[test]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()));

        mvc.perform(MockMvcRequestBuilders.get("/game/1/player/test/score").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    public void testGetTotalScoreOneCompleteFrame() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        JSONArray json = new JSONArray("[test]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()));

        mvc.perform(MockMvcRequestBuilders.put("/game/1/player/test/roll/4").accept(MediaType.APPLICATION_JSON));

        mvc.perform(MockMvcRequestBuilders.put("/game/1/player/test/roll/4").accept(MediaType.APPLICATION_JSON));

        mvc.perform(MockMvcRequestBuilders.get("/game/1/player/test/score").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("8"));
    }

    @Test
    public void testGetTotalScorePerfectGame() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/game").accept(MediaType.APPLICATION_JSON));

        JSONArray json = new JSONArray("[test]");
        mvc.perform(MockMvcRequestBuilders.post("/game/1/player").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()));

        for (int i=0; i< 12; i++) {
            mvc.perform(MockMvcRequestBuilders.put("/game/1/player/test/roll/10").accept(MediaType.APPLICATION_JSON));
        }

        mvc.perform(MockMvcRequestBuilders.get("/game/1/player/test/score").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("300"));
    }


}
