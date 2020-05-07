package com.robot.bowlingscore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.robot.bowlingscore.model.Game;
import com.robot.bowlingscore.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PlayerRepositoryTests {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    private Player p;

    @Before
    public void setUp() {
        p = new Player();
        p.setName("Test");
    }

    @After
    public void tearDown() {
        p = null;
    }

    @Test
    public void createPlayer() {
        playerRepo.save(p);
        assertThat(playerRepo.findByName(p.getName()).getName()).isEqualTo(p.getName());
    }

    @Test
    public void addGameToPlayer() {
        playerRepo.save(p);
        Game g = new Game();
        gameRepo.save(g);

        p.setGame(g);
        playerRepo.save(p);

        assertThat(playerRepo.findByNameAndGameId(p.getName(), g.getId()).getName()).isEqualTo(p.getName());
        assertThat(playerRepo.findByNameAndGameId(p.getName(), g.getId()).getGame().getId()).isEqualTo(p.getGame().getId());
    }

    @Test
    public void testNoPlayersPersisted() {
        assertThat(playerRepo.findByName("test")).isEqualTo(null);
    }

}
