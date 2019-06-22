package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreController {

    private static final int FIRSTBLOOD_POINTS = 1;
    private static final Integer[] DAMAGE_POINTS = new Integer[] {8, 6, 4, 2, 1, 1};
    private static final int LOWEST_DAMAGE_POINTS = 1;
    private static final int OVERKILL_MARKS = 1;
    private static final int MULTIKILL_POINTS = 1;

    private Match match;
    private List<PlayerToken> killedSomeone;

    public ScoreController(Match match){
        this.match = match;
    }

    /**
     * Cycles through all the players and in case someone is dead, proceeds to calculate points and manages the tokens
     * that should be moved in these situations.
     */
    public void lookForScoreUpdates(){

        killedSomeone = new ArrayList<>();

        for(Player player : match.getPlayers()){
            if(player.isDead()){
                assignPlayerBoardScore(player);
                fixTokensAndThings(player);
            }
        }
    }

    /**
     * When the game ends, all the player boards should be calculated
     */
    public void endGamePoints(){
        for(Player player : match.getPlayers()){
            assignPlayerBoardScore(player);
        }
    }

    /**
     * Assigns the correct amount of points to all the players who deserve some
     * @param player The poor dead player
     */
    private void assignPlayerBoardScore(Player player){
        // Assign the first blood points
        match.getPlayerByTokenColor(player.getFirstBlood()).addPoints(FIRSTBLOOD_POINTS);

        // Assign damage points
        // Firstly we create a List containing all the player that damaged the victim, ordered by damage inflicted
        List<Player> damagers = player.getDamageOrder().stream()
                .map(tkn -> match.getPlayerByTokenColor(tkn))
                .collect(Collectors.toList());
        // For each of these players, assign the correct amount of damage
        for(Player playerDamage : damagers){
            int pointsIndex = player.getSkulls() + damagers.indexOf(playerDamage);
            if(pointsIndex < DAMAGE_POINTS.length)
                playerDamage.addPoints(DAMAGE_POINTS[pointsIndex]);
            else
                playerDamage.addPoints(LOWEST_DAMAGE_POINTS);
        }

        // To verify multikills
        if(killedSomeone.contains(player.getLethalDamage()))
            match.getPlayerByTokenColor(player.getLethalDamage()).addPoints(MULTIKILL_POINTS);
        else
            killedSomeone.add(player.getLethalDamage());
    }

    /**
     * As name suggests, fixes the Gameboard and the player card by putting tokens and skulls where they are supposed
     * to be placed.
     * @param player The poor dead player
     */
    private void fixTokensAndThings(Player player){
        // Assign overkill marks
        if(player.getOverkill()!=null)
            match.getPlayerByTokenColor(player.getOverkill()).addMark(player.getColor(), OVERKILL_MARKS);

        // Add a skull to the dead player
        player.addSkull();

        // Add the kill to the Gameboard
        List<PlayerToken> killer = new ArrayList<>();
        killer.add(player.getLethalDamage());
        match.getGameBoard().addKill(killer);

        // Finally, reset the damage, so that the player can play again
        player.resetDamage();
    }
}
