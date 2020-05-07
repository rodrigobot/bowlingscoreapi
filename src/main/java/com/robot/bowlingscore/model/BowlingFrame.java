package com.robot.bowlingscore.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * Entity class that holds information about a bowling frame.
 *
 * @author Rodrigo Soto
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BowlingFrame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Association of multiple frames per player
    @ManyToOne
    @JoinColumn(name = "playerName")
    private Player player;

    private int firstRoll = -1;
    private int secondRoll = -1;
    private int score = -1;
    private int rolls = 0;
    private boolean spare = false;
    private boolean strike = false;
    private boolean completeFrame = false;

    // This 2 variables will help in the special case for bonus balls on the tenth frame
    private boolean bonusBalls = false;
    private int bonusBall = -1;

    public Long getId() {
        return id;
    }

    public void setFirstRoll(int pins){
        firstRoll = pins;
    }

    public int getFirstRoll(){ return firstRoll; }

    public void setSecondRoll(int pins) {
        secondRoll = pins;
    }

    public int getSecondRoll() { return secondRoll; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setSpare(boolean spare) {
        this.spare = spare;
    }

    public boolean isSpare() {
        return spare;
    }

    public void setStrike(boolean strike) {
        this.strike = strike;
    }

    public boolean isStrike() {
        return strike;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCompleteFrame() {
        return completeFrame;
    }

    public void setCompleteFrame(boolean completeFrame) {
        this.completeFrame = completeFrame;
    }

    public int getRolls() {
        return rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = rolls;
    }

    public boolean isBonusBalls() {
        return bonusBalls;
    }

    public void setBonusBalls(boolean bonusBalls) {
        this.bonusBalls = bonusBalls;
    }

    public int getBonusBall() {
        return bonusBall;
    }

    public void setBonusBall(int bonusBall) {
        this.bonusBall = bonusBall;
    }
}
