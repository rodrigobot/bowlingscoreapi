package com.robot.bowlingscore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SpecialFrame")
public class SpecialBonusFrame extends AbstractFrame {
    public void calculateScore() {
        if (!isCompleteFrame() && getFirstRoll() >-1 && getSecondRoll() > -1 && getBonusBall() > -1) {
            setScore(getSecondRoll() + getSecondRoll() + getBonusBall());
            setCompleteFrame(true);
        } else if (!isCompleteFrame() && getFirstRoll() > -1 && getSecondRoll() > -1) {
            setScore(getFirstRoll() + getSecondRoll());
        } else if (!isCompleteFrame() && getFirstRoll() > -1) {
            setScore(getFirstRoll());
        }
    }
}
