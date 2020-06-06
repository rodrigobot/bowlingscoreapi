package com.robot.bowlingscore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Strike")
public class StrikeFrame extends AbstractFrame {

    public void calculateScore(int score1, int score2) {
        if (!isCompleteFrame() && score1 > -1 && score2 >1) {
            this.score = getFirstRoll() + score1 + score2;
            setCompleteFrame(true);
        } else if (!isCompleteFrame() && score1 >1){
            this.score = getFirstRoll() + score1;
        } else if (!isCompleteFrame()) {
            this.score = getFirstRoll();
        }
    }
}
