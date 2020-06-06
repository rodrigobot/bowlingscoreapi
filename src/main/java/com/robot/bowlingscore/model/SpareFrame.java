package com.robot.bowlingscore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Spare")
public class SpareFrame extends AbstractFrame {

    public SpareFrame() {
        super();
    }
    public SpareFrame(AbstractFrame frame) {
        this.setScore(frame.getScore());
        this.setCompleteFrame(frame.isCompleteFrame());
        this.setRolls(frame.getRolls());
        this.setSecondRoll(frame.getSecondRoll());
        this.setBonusBalls(frame.isBonusBalls());
        this.setBonusBall(frame.getBonusBall());
        this.setFirstRoll(frame.getFirstRoll());
        this.setPlayer(frame.getPlayer());
    }
}
