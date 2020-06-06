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
public class PlayerTests {

    @Autowired
    private TestEntityManager entityManager;

    private Player p;

    @Before
    public void setUp() {
        p = new Player("Test");
    }

    @After
    public void tearDown() {
        p = null;
    }

    @Test
    public void testCreatePlayer() {
        Player savedPlayer = entityManager.persistAndFlush(p);

        assertThat(savedPlayer.getName()).isEqualTo("Test");
    }

    @Test
    public void testCreatePlayerSetGame() {
        Player savedPlayer = entityManager.persistAndFlush(p);
        Game g = new Game();
        entityManager.persistAndFlush(g);

        p.setGame(g);
        savedPlayer = entityManager.persistFlushFind(p);

        assertThat(savedPlayer.getGame().getId()).isEqualTo(1L);
    }

    @Test
    public void testSetFrames() {
        List<AbstractFrame> frames = new ArrayList<>();
        BowlingFrame frame = new BowlingFrame();
        frames.add(frame);
        p.setFrames(frames);
        Player savedPlayer = entityManager.persistAndFlush(p);

        assertThat(savedPlayer.getFrames().size()).isEqualTo(1);
    }
}
