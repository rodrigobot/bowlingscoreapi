package com.robot.bowlingscore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.robot.bowlingscore.model.Game;
import com.robot.bowlingscore.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;


@RunWith(SpringRunner.class)
@DataJpaTest
public class GameRepositoryTests {
    @Autowired
    private GameRepository gameRepo;

    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @After
    public void tearDown() {
        game = null;
    }

    @Test
    public void createANewGame() throws Exception {
        gameRepo.save(game);
        assertThat(gameRepo.findById(game.getId().longValue())).isEqualTo(game);
    }

    @Test
    public void updateGameWithNewPlayer() throws Exception {
        gameRepo.save(game);
        Player p = new Player("test");
        p.setGame(game);
        gameRepo.save(game);

        assertThat(gameRepo.findById(game.getId().longValue())).isEqualTo(game);
    }
}
