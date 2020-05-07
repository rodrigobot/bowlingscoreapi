package com.robot.bowlingscore.controllers;

import com.robot.bowlingscore.model.Game;
import com.robot.bowlingscore.model.Player;
import com.robot.bowlingscore.repository.PlayerRepository;
import com.robot.bowlingscore.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;


/**
 * Routes for the Bowling Score API
 *
 * @author Rodrigo Soto
 */
@RestController
@SessionScope
public class BowlingScoreController {

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepository rep;

    /**
     * Get welcome message at index
     *
     * @return Welcome message
     */
    @GetMapping("/")
    public String index(){
        return "Welcome to the Bowling Score!!!\n";
    }

    /**
     * Initialize the game with zero players.
     *
     * @return A Json representation of a Game which contains and ID and an empty list of players
     */
    @PostMapping(path="/game")
    public Game initializeGame() {
        return gameService.create();
    }

    /**
     * Retrieve game information based on a Game ID
     *
     * @param gameId - the ID of the game
     * @return A Json representation of a game which contains an ID, list of players and frames per player
     */
    @GetMapping("/game/{gameId}")
    public Game getGameInformation(@PathVariable("gameId") long gameId) {
        return gameService.getGame(gameId);
    }

    /**
     * Add a player or players to a game by game ID. This service accepts a JSON array with the players name.
     *
     * @param gameId - the ID of the game
     * @param names - A Json array containing the list of players.
     * @return A Json representation of a game which contains and id, a list of players and a list frames per player
     */
    @PostMapping(path = "/game/{id}/player", consumes = "application/json")
    public Game addPlayer(@PathVariable("id") long gameId, @RequestBody List<String> names) {
        return gameService.addPlayers(gameId, names);
    }

    /**
     * Add a roll (pins knockdown) to a player by game id and player name
     * @param gameId - A game ID
     * @param name - player's name
     * @param pins - pins knockdown
     * @return A player representation with its respective frames, score by frame and frame type. Frame types are:
     *         1. strike
     *         2. spare
     *         3. special case for 10th frame.
     */
    @PutMapping("/game/{gameId}/player/{playerName}/roll/{pins}")
    public Player roll(@PathVariable("gameId") long gameId,
                    @PathVariable("playerName") String name,
                    @PathVariable("pins") int pins
    ) {
        return gameService.score(gameId, name, pins);
    }

    /**
     * Get the total score by game and player's name
     *
     * @param gameId - a game id
     * @param name - a player's name
     * @return An integer representing the total score.
     */
    @GetMapping("/game/{gameId}/player/{playerName}/score")
    public int getTotalScore(@PathVariable("gameId") long gameId,
                             @PathVariable("playerName") String name
    ){
        return gameService.getTotalScore(gameId, name);
    }
}
