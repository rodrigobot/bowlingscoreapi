package com.robot.bowlingscore.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GameTests {

    @Autowired
    private TestEntityManager entityManager;

    private Player p;

    private Game g;

    @Before
    public void setUp() {
        p = new Player("test");
        entityManager.persist(p);
        g = new Game();
    }

    @After
    public void tearDown() {
        p = null;
        g = null;
    }

    @Test
    public void createEntityGame() {
        Game savedData = entityManager.persistAndFlush(g);
        assertThat(savedData.getId()).isEqualTo(1L);
    }

    @Test
    public void createGameAddPlayers() {
        Game savedData = entityManager.persistAndFlush(g);

        List<Player> players = new ArrayList<>();
        p.setGame(savedData);
        players.add(p);

        savedData = entityManager.persistFlushFind(savedData);

        assertThat(savedData.getId()).isEqualTo(1L);
        assertThat(savedData.getPlayers().size()).isEqualTo(1);
        assertThat(savedData.getPlayers().get(0).getName()).isEqualTo("test");
    }

    @Test
    public void testGetGameId() {
        Game savedData = entityManager.persistFlushFind(g);
        assertThat(savedData.getId()).isEqualTo(1L);
    }

    @Test
    public void testGetEmptyPlayersList() {
        Game savedData = entityManager.persistFlushFind(g);

        assertThat(savedData.getPlayers().size()).isEqualTo(0);
    }
}
