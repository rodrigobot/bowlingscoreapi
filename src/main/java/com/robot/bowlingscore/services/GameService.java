package com.robot.bowlingscore.services;

import com.robot.bowlingscore.errors.GameCompleteException;
import com.robot.bowlingscore.model.*;
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
            List<AbstractFrame> frames = player.getFrames();
            if (frames.size() == 10 && frames.get(9).isCompleteFrame()) {
                throw new GameCompleteException("Game has finished");
            }

            if ((frames.size() == 0 || frames.get(frames.size()-1).getRolls() == 2) && frames.size() != 10) {
                if (!isRollStrike(pins)) {
                    AbstractFrame temp = new BowlingFrame();
                    temp.setFirstRoll(pins);
                    temp.setPlayer(player);
                    temp.setRolls(1);
                    frames.add(temp);
                } else if (frames.size() == 9) {
                    AbstractFrame temp = new SpecialBonusFrame();
                    temp.setFirstRoll(pins);
                    temp.setPlayer(player);
                    temp.setRolls(1);
                    frames.add(temp);
                } else {
                    AbstractFrame temp = new StrikeFrame();
                    temp.setFirstRoll(pins);
                    temp.setPlayer(player);
                    temp.setRolls(2);
                    frames.add(temp);
                }
            } else if (frames.get(frames.size()-1).getRolls() == 1 && frames.size() != 10) {
                AbstractFrame temp = frames.get(frames.size() - 1);
                temp.setRolls(2);
                temp.setSecondRoll(pins);
                temp = isRollSpare(temp.getFirstRoll(), temp.getSecondRoll()) ? new SpareFrame(temp) : temp;
                frames.set(frames.size()-1, temp);
            } else if (frames.size() == 10 && frames.get(9) instanceof SpecialBonusFrame && frames.get(9).getRolls() == 1) {
                AbstractFrame temp = frames.get(9);
                temp.setRolls(2);
                temp.setSecondRoll(pins);
            } else if (frames.size() == 10 && frames.get(9) instanceof SpecialBonusFrame && frames.get(9).getRolls() == 2) {
                AbstractFrame temp = frames.get(9);
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

    private List<AbstractFrame> computeScore(List<AbstractFrame> frames) {
        for (int i =frames.size()-1; i >= 0; i--) {
            AbstractFrame currentFrame = frames.get(i);
            if (frames.size() == 10) {
                if (currentFrame instanceof BowlingFrame) {
                    ((BowlingFrame) currentFrame).calculateScore();
                } else if (currentFrame instanceof SpecialBonusFrame) {
                    ((SpecialBonusFrame) currentFrame).calculateScore();
                }
            }

            if (currentFrame instanceof BowlingFrame) {
                ((BowlingFrame) currentFrame).calculateScore();
            } else if (currentFrame instanceof StrikeFrame && i == frames.size()-1){
                ((StrikeFrame) currentFrame).calculateScore(-1, -1);
            } else if (currentFrame instanceof StrikeFrame && i < frames.size()-1){
                AbstractFrame nextFrame = frames.get(i+1);
                if (nextFrame instanceof SpareFrame){
                    ((StrikeFrame) currentFrame).calculateScore(nextFrame.getFirstRoll(), nextFrame.getSecondRoll());
                } else if (nextFrame instanceof StrikeFrame && i+2 <= frames.size()-1) {
                    ((StrikeFrame) currentFrame).calculateScore(nextFrame.getFirstRoll(), frames.get(i+2).getFirstRoll());
                } else if (nextFrame instanceof SpecialBonusFrame){
                    ((StrikeFrame) currentFrame).calculateScore(nextFrame.getFirstRoll(), nextFrame.getSecondRoll());
                } else {
                    ((StrikeFrame) currentFrame).calculateScore(nextFrame.getFirstRoll(), -1);
                }
            } else if (currentFrame instanceof SpareFrame && i == frames.size()-1) {
                ((SpareFrame) currentFrame).calculateScore(-1);
            } else if (currentFrame instanceof SpareFrame && i < frames.size()-1){
                AbstractFrame nextFrame = frames.get(i+1);
                ((SpareFrame) currentFrame).calculateScore(nextFrame.getFirstRoll());
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
            List<AbstractFrame> frames = player.getFrames();
            int total = 0;
            for (int i = 0; i < frames.size(); i++) {
                total += frames.get(i).getScore();
            }

            return total;
        }

        return -100;
    }
}
