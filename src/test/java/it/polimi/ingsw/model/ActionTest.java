package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {
    private Action actA;
    private Action actB;
    private Player player;

    @BeforeEach
    void setup(){
        actA = new Action();
        actB = new Action();

        actA.addAction(BasicAction.GRAB);
        actA.addAction(BasicAction.MOVEMENT);
        actA.addAction(BasicAction.MOVEMENT);

        actB.addAction(BasicAction.RELOAD);
        actB.addAction(BasicAction.RELOAD);
        actB.addAction(BasicAction.SHOOT);

        player = new Player("Ada", PlayerToken.YELLOW);
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

    @Test
    void fromResourceTest(){
        List<Action> actions = ActionSupplier.getInstance().getActions();

        Action act = actions.get(0);

        assertEquals(act.countMovement(), 3);
        assertFalse(act.getBeforeFirstPlayer());
        assertEquals(act.getDamageOverwrite(), 99);
        assertEquals(act.getDamageRequired(), 0);

        act = actions.get(5);
        assertFalse(act.canReload());
        assertFalse(act.canShoot());
        assertFalse(act.canMove());
        assertEquals(act.countMovement(), 0);
        assertEquals(act.getDamageOverwrite(), 99);

        act = actions.get(8);
        assertEquals(act.countMovement(), 2);
        assertTrue(act.canGrab());
        assertFalse(act.canShoot());
        assertEquals(act.getDamageOverwrite(), 99);
        assertTrue(act.getFinalFrenzyRequired());
        assertFalse(act.getBoardNotFlippedRequired());
    }

    @Test
    void canPerformTest(){
        Action act = new Action();

        // Standard action, no FF, no damage
        act.setDamageRequired(0);
        act.setDamageOverwrite(3);
        act.addAction(BasicAction.MOVEMENT);
        assertTrue(act.canPerform(player, false));
        assertFalse(act.canPerform(player, true));

        // Now shouldn't work cause we have a better option
        player.addDamageWithoutMarks(PlayerToken.GREY, 8);
        assertFalse(act.canPerform(player, false));
        assertFalse(act.canPerform(player, true));

        // Powerful action unlocked, requires a certain amount of damage
        act.setDamageOverwrite(99);
        act.setDamageRequired(3);
        act.setBoardNotFlippedRequired(true);
        player.resetDamage();
        // But still we did not take enough damage
        player.addDamageWithoutMarks(PlayerToken.GREY, 2);
        assertFalse(act.canPerform(player, false));
        assertFalse(act.canPerform(player, true));

        // Ok now we're ready, injured enough
        player.addDamageWithoutMarks(PlayerToken.GREY, 1);
        assertTrue(act.canPerform(player, false));
        player.addDamageWithoutMarks(PlayerToken.GREY, 2);
        assertTrue(act.canPerform(player, false));

        // If we are in Final Frenzy, I cannot do these powerful actions
        assertFalse(act.canPerform(player, true));

        // If the board has been flipped, I cannot do the action I unlocked before
        player.flipBoard();
        assertFalse(act.canPerform(player, false));

        // Now testing frenzy actions
        act.setDamageOverwrite(99);
        act.setDamageRequired(0);
        act.setBoardNotFlippedRequired(false);
        act.setFinalFrenzyRequired(true);
        assertTrue(act.canPerform(player, true));

        // Some actions can be performed only by players playing their FF turn before the first player
        act.setBeforeFirstPlayer(true);
        assertFalse(act.canPerform(player, true));
        player.setBeforeFistPlayerFF(true);
        assertTrue(act.canPerform(player, true));

        assertFalse(act.canPerform(player, false));

        act.setBeforeFirstPlayer(false);
        player.setBeforeFistPlayerFF(true);
        assertFalse(act.canPerform(player, true));

    }
}