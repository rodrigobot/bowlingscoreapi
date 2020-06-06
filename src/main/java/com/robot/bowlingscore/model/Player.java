package com.robot.bowlingscore.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Entity class that holds information about a player
 *
 * @author Rodrigo Soto
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Player {
    @Id
    private String name;

    // Association of a player and bowling frames
    @OneToMany(mappedBy = "player",cascade = CascadeType.ALL, targetEntity = AbstractFrame.class, fetch = FetchType.LAZY)
    private List<AbstractFrame> frames = new ArrayList<>(10);

    // Association of a player and multiple games
    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    public Player() { }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AbstractFrame> getFrames() {
        return frames;
    }

    public void setFrames(List<AbstractFrame> frames) { this.frames = frames; }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
