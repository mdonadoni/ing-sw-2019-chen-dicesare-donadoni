package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
    private Action actA;
    private Action actB;

    @BeforeEach
    void setup(){
        actA = new Action();
        actB = new Action();

        actA.addAction(BaseAction.GRAB);
        actA.addAction(BaseAction.MOVEMENT);
        actA.addAction(BaseAction.MOVEMENT);

        actB.addAction(BaseAction.RELOAD);
        actB.addAction(BaseAction.RELOAD);
        actB.addAction(BaseAction.SHOOT);
    }

    @Test
    void countMovement() {
        assertEquals(actA.countMovement(), 2);
        assertEquals(actB.countMovement(), 0);
    }

    @Test
    void canMove() {
        assertTrue(actA.canMove());
        assertFalse(actB.canMove());
    }

    @Test
    void canGrab() {
        assertTrue(actA.canGrab());
        assertFalse(actB.canGrab());
    }

    @Test
    void canShoot() {
        assertFalse(actA.canShoot());
        assertTrue(actB.canShoot());
    }

    @Test
    void canReload() {
        assertFalse(actA.canReload());
        assertTrue(actB.canReload());
    }
}