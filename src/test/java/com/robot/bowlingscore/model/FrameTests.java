package com.robot.bowlingscore.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FrameTests {

    @Autowired
    private TestEntityManager entityManager;

    private AbstractFrame frame;

    @Before
    public void setUp() {
        frame = new BowlingFrame();
    }

    @After
    public void tearDown() {
        frame = null;
    }

    @Test
    public void testPersistNewFrame() {
        AbstractFrame savedData = entityManager.persistFlushFind(frame);

        assertThat(savedData.getId()).isEqualTo(1L);
    }


    @Test
    public void testSetIsSpareAndIsSpare() {
        SpareFrame spare = new SpareFrame();
        AbstractFrame savedData = entityManager.persistFlushFind(spare);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData instanceof SpareFrame);
    }


    @Test
    public void testSetIsStrikeAndIsStrike() {
        StrikeFrame strike = new StrikeFrame();
        AbstractFrame savedData = entityManager.persistFlushFind(strike);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData instanceof StrikeFrame);
    }

    @Test
    public void testSetBonusBallAndIsBonusBall() {
        AbstractFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setBonusBalls(true);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.isBonusBalls()).isEqualTo(true);
    }

    @Test
    public void testSetFirstRollAndGetFirstRoll() {
        AbstractFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setFirstRoll(4);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getFirstRoll()).isEqualTo(4);
    }

    @Test
    public void testSetSecondRollAndGetSecondRoll() {
        AbstractFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setSecondRoll(9);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getSecondRoll()).isEqualTo(9);
    }

    @Test
    public void testSetScoreAndGetScore() {
        AbstractFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setScore(10);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getScore()).isEqualTo(10);
    }

    @Test
    public void testSetRollsAndGetRolls() {
        AbstractFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setRolls(2);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getRolls()).isEqualTo(2);
    }

    @Test
    public void testSetIsCompleteAndIsComplete() {
        AbstractFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setCompleteFrame(true);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.isCompleteFrame()).isEqualTo(true);
    }
}
