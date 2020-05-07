package com.robot.bowlingscore.services;

import com.robot.bowlingscore.errors.GameCompleteException;
import com.robot.bowlingscore.model.BowlingFrame;
import com.robot.bowlingscore.model.Game;
import com.robot.bowlingscore.model.Player;
import com.robot.bowlingscore.repository.GameRepository;
import com.robot.bowlingscore.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Game Service used to manage a game of bowling. This service provides a way to start a game, retrieve game information,
 * retrieve total score, and compute a score, add player or players. A game consists of one or more players.
 * During the game a player will roll and knock down pins which will be sent to this service to keep count and
 * compute a player's score.
 *
 * @author Rodrigo Soto
 */
@Service
public class GameService {
    private final GameRepository gameRepo;
    private final PlayerRepository playerRepo;

    /**
     * Constructor for service class used to manage a bowling game.
     *
     * @param gr - Game Repository which manages each entity associated to a game
     * @param pr - Player Repository which manages each entity associated to a player in a game
     */
    public GameService(GameRepository gr, PlayerRepository pr) {
        this.gameRepo = gr;
        this.playerRepo = pr;
    }

    /**
     * Initializes a game with no players and no frames
     *
     * @return An object game representation
     */
    public Game create() {
        return gameRepo.save(new Game());
    }

    /**
     * Add a list of players to a game
     *
     * @param id - game id that will be use to add players
     * @param names - a list of names of the players to be added to a game
     * @return An object representation of a game with the players added
     */
    public Game addPlayers(long id, List<String> names) {
        Game temp = gameRepo.findById(id);
        if (temp != null) {
            List<Player> players = temp.getPlayers();
            for (String name : names) {
                Player p = new Player(name);
                p.setGame(temp);
                players.add(p);
            }

            temp.setPlayers(players);
            return gameRepo.save(temp);
        }

        return null;
    }

    /**
     * Retrieve a game's information
     *
     * @param id - a game id
     * @return An object game representation
     */
    public Game getGame(long id) {
        return gameRepo.findById(id);
    }

    /**
     * Calculates the score of all "incomplete" frames. A frames is considered incomplete if there are rolls to be made
     * by the player. For example:
     *         * If a frame is marked as a strike or spare, the total score will not be computed until the next 2 rolls
     *           have been performed, therefore the frame is considered incomplete.
     *         * If the player makes "an open frame" meaning the first and second roll sum is less than 10, the frame
     *           will be considered complete and the total score for that frame will be the sum of the first and second roll
     *
     * @param gameId - a game id
     * @param name - player's name
     * @param pins - number of pins knockdown
     * @return An object player's representation with the frames list and game id
     */
    public Player score(long gameId, String name, int pins) {
        // find player for given name and game if
        Player player = playerRepo.findByNameAndGameId(name, gameId);

        // add roll to current frame or create new frame
        if (player != null) {
            List<BowlingFrame> frames = player.getFrames();
            if (frames.size() == 10 && frames.get(9).isCompleteFrame()) {
                throw new GameCompleteException("Game has finished");
            }

            if ((frames.size() == 0 || frames.get(frames.size()-1).getRolls() == 2) && frames.size() != 10) {
                BowlingFrame temp = new BowlingFrame();
                temp.setFirstRoll(pins);
                temp.setStrike(isRollStrike(temp.getFirstRoll()));
                temp.setPlayer(player);
                temp.setRolls(temp.isStrike() ? 2 : 1);
                frames.add(temp);
            } else if (frames.get(frames.size()-1).getRolls() == 1 && frames.size() != 10) {
                BowlingFrame temp = frames.get(frames.size() - 1);
                temp.setRolls(2);
                temp.setSecondRoll(pins);
                temp.setSpare(isRollSpare(temp.getFirstRoll(), temp.getSecondRoll()));
            } else if (frames.size() == 10 && frames.get(9).isStrike() && frames.get(9).getSecondRoll() == -1) {
                BowlingFrame temp = frames.get(9);
                temp.setBonusBalls(true);
                temp.setSecondRoll(pins);
            } else if (frames.size() == 10 && frames.get(9).isStrike() && frames.get(9).getSecondRoll() != -1) {
                BowlingFrame temp = frames.get(9);
                temp.setBonusBall(pins);
            }

            // calculate score peer frame
            player.setFrames(computeScore(frames));
            return playerRepo.save(player);
        }

        return null;
    }

    private boolean isRollStrike(int firstRoll) {
        return firstRoll == 10 ? true : false;
    }

    private boolean isRollSpare(int fr, int sr) {
        return fr + sr == 10 ? true : false;
    }

    private List<BowlingFrame> computeScore(List<BowlingFrame> frames) {
        for (int i =frames.size()-1; i >= 0; i--) {
            BowlingFrame currentFrame = frames.get(i);
            if (frames.size() == 10) {
                if (!currentFrame.isStrike() && !currentFrame.isSpare()) {
                    currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll());
                    currentFrame.setCompleteFrame(true);
                } else if(currentFrame.getSecondRoll() != -1 && currentFrame.getBonusBall() != -1) {
                    currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll() + currentFrame.getBonusBall());
                    currentFrame.setCompleteFrame(true);
                } else if (currentFrame.getSecondRoll() != -1 && currentFrame.getBonusBall() == -1) {
                    currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll());
                } else if (currentFrame.getSecondRoll() != -1) {
                    currentFrame.setScore(currentFrame.getFirstRoll());
                }
            }

            if (!currentFrame.isCompleteFrame() && !currentFrame.isSpare()
                    && !currentFrame.isStrike() && currentFrame.getRolls() == 2
            ) {
                currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll());
                currentFrame.setCompleteFrame(true);
            } else if (!currentFrame.isCompleteFrame() && !currentFrame.isSpare() && !currentFrame.isStrike() && currentFrame.getRolls() ==1) {
                currentFrame.setScore(currentFrame.getFirstRoll());
            } else if (!currentFrame.isCompleteFrame() && currentFrame.isStrike() && i == frames.size()-1){
                currentFrame.setScore(currentFrame.getFirstRoll());
            } else if (!currentFrame.isCompleteFrame() && currentFrame.isSpare() && i == frames.size()-1) {
                currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll());
            } else if (!currentFrame.isCompleteFrame() && currentFrame.isStrike() && i < frames.size()-1){
                BowlingFrame nextFrame = frames.get(i+1);
                if (nextFrame.getSecondRoll() > -1){
                    currentFrame.setScore(currentFrame.getFirstRoll() + nextFrame.getFirstRoll() + nextFrame.getSecondRoll());
                    currentFrame.setCompleteFrame(true);
                } else if (nextFrame.isStrike() && i+2 <= frames.size()-1) {
                    currentFrame.setScore(currentFrame.getFirstRoll() + nextFrame.getFirstRoll() + frames.get(i+2).getFirstRoll());
                    currentFrame.setCompleteFrame(true);
                } else {
                    currentFrame.setScore(currentFrame.getFirstRoll() + nextFrame.getFirstRoll());
                }
            } else if (!currentFrame.isCompleteFrame() && currentFrame.isSpare() && i < frames.size()-1){
                BowlingFrame nextFrame = frames.get(i+1);
                currentFrame.setScore(currentFrame.getFirstRoll() + currentFrame.getSecondRoll() + nextFrame.getFirstRoll());
                currentFrame.setCompleteFrame(true);
            }
        }

        return frames;
    }

    /**
     * Retrieve the total score of a game by player's name
     *
     * @param gameId - a game id
     * @param name - player's name
     * @return An integer representing the total score of the game
     */
    public int getTotalScore (long gameId, String name) {
        Player player = playerRepo.findByNameAndGameId(name, gameId);
        if (player != null) {
            List<BowlingFrame> frames = player.getFrames();
            int total = 0;
            for (int i = 0; i < frames.size(); i++) {
                total += frames.get(i).getScore();
            }

            return total;
        }

        return -100;
    }
}
