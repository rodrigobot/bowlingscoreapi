package com.robot.bowlingscore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Bowling")
public class BowlingFrame extends AbstractFrame {

    public void calculateScore() {
        if (getSecondRoll() > -1) {
            this.score = getFirstRoll() + getSecondRoll();
            this.setCompleteFrame(true);
        } else {
            this.score = getFirstRoll();
        }
    }
}
