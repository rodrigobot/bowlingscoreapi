package com.robot.bowlingscore.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Strike")
public class StrikeFrame extends AbstractFrame {
}
