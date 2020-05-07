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

    private BowlingFrame frame;

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
        BowlingFrame savedData = entityManager.persistFlushFind(frame);

        assertThat(savedData.getId()).isEqualTo(1L);
    }

    @Test
    public void testSetIsSpareAndIsSpare() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setSpare(true);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.isSpare()).isEqualTo(true);
    }

    @Test
    public void testSetIsStrikeAndIsStrike() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setStrike(true);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.isStrike()).isEqualTo(true);
    }

    @Test
    public void testSetBonusBallAndIsBonusBall() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setBonusBalls(true);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.isBonusBalls()).isEqualTo(true);
    }

    @Test
    public void testSetFirstRollAndGetFirstRoll() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setFirstRoll(4);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getFirstRoll()).isEqualTo(4);
    }

    @Test
    public void testSetSecondRollAndGetSecondRoll() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setSecondRoll(9);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getSecondRoll()).isEqualTo(9);
    }

    @Test
    public void testSetScoreAndGetScore() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setScore(10);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getScore()).isEqualTo(10);
    }

    @Test
    public void testSetRollsAndGetRolls() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setRolls(2);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.getRolls()).isEqualTo(2);
    }

    @Test
    public void testSetIsCompleteAndIsComplete() {
        BowlingFrame savedData = entityManager.persistFlushFind(frame);
        savedData.setCompleteFrame(true);
        savedData = entityManager.persistFlushFind(savedData);
        assertThat(savedData.isCompleteFrame()).isEqualTo(true);
    }
}
