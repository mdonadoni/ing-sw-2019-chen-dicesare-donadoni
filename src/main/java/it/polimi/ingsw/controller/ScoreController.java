package it.polimi.ingsw.controller;

import it.polimi.ingsw.common.StandingsItem;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerToken;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller that calculates the score and handles players' death
 */
public class ScoreController {

    /**
     * Points given to whom gets the firstblood
     */
    private static final int FIRSTBLOOD_POINTS = 1;
    /**
     * Standard array of scores for the damage dealt to a dead player
     */
    private static final Integer[] DAMAGE_POINTS = new Integer[] {8, 6, 4, 2, 1, 1};
    /**
     * Points given for the damage dealt to a dead player with the board flipped
     */
    private static final Integer[] BOARD_FLIPPED_POINTS = new Integer[] {2, 1, 1, 1};
    /**
     * Array of points given based on the number of kills on the KillShotTrack
     */
    private static final Integer[] KILLSHOT_TRACK_POINTS = new Integer[] {8, 6, 4, 2, 1, 1};
    /**
     * Lowest possible points given for the damage dealt to a dead player
     */
    private static final int LOWEST_DAMAGE_POINTS = 1;
    /**
     * Lowest possible points given to players for the kills on the KillShotTrack
     */
    private static final int LOWEST_KILLSHOT_TRACK_POINTS = 1;
    /**
     * Number of marks taken for an overkill
     */
    private static final int OVERKILL_MARKS = 1;
    /**
     * Number of points given for a multikill
     */
    private static final int MULTIKILL_POINTS = 1;

    /**
     * The match going on
     */
    private Match match;
    /**
     * List containing the players that killed someone during this turn
     */
    private List<PlayerToken> killedSomeone;

    /**
     * Constructor
     * @param match The match going on
     */
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
                verifyMultikills(player);
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

        // Calculate points from the killshot track
        List<Player> killers = match.getGameBoard().getKillShotTrackOrder().stream()
                .map(tkn -> match.getPlayerByTokenColor(tkn))
                .collect(Collectors.toList());
        for(Player playerDamage : killers){
            int pointsIndex = killers.indexOf(playerDamage);
            if(pointsIndex < KILLSHOT_TRACK_POINTS.length)
                playerDamage.addKillShotTrackPoints(KILLSHOT_TRACK_POINTS[pointsIndex]);
            else
                playerDamage.addKillShotTrackPoints(LOWEST_KILLSHOT_TRACK_POINTS);
        }
    }

    /**
     * Generate the final standings.
     * @return List of StandingsItem.
     */
    public List<StandingsItem> getFinalStandings() {
        Comparator<Player> comparePoints = (p1, p2) -> {
            if (p1.getTotalPoints() == p2.getTotalPoints()) {
                return p2.getKillShotTrackPoints() - p1.getKillShotTrackPoints();
            }
            return p2.getTotalPoints() - p1.getTotalPoints();
        };

        List<Player> orderedPlayers = match.getPlayers().stream()
                .sorted(comparePoints)
                .collect(Collectors.toList());

        List<StandingsItem> standings = new ArrayList<>();

        int lastPosition = 0;
        for (int i = 0; i < orderedPlayers.size(); i++) {
            Player p = orderedPlayers.get(i);

            int position = lastPosition;
            if (i == 0 || comparePoints.compare(orderedPlayers.get(i-1), p) != 0) {
                position = standings.size()+1;
            }

            lastPosition = position;
            standings.add(new StandingsItem(position, p.getNickname(), p.getTotalPoints(), p.getColor()));
        }

        return standings;
    }

    /**
     * Assigns the correct amount of points to all the players who deserve some
     * @param player The poor dead player
     */
    private void assignPlayerBoardScore(Player player){
        // Assign the first blood points
        if(!player.getDamageTaken().isEmpty() && !player.isBoardFlipped())
            match.getPlayerByTokenColor(player.getFirstBlood()).addPoints(FIRSTBLOOD_POINTS);

        // Assign damage points
        // Firstly we create a List containing all the player that damaged the victim, ordered by damage inflicted
        List<Player> damagers = player.getDamageOrder().stream()
                .map(tkn -> match.getPlayerByTokenColor(tkn))
                .collect(Collectors.toList());
        // For each of these players, assign the correct amount of damage
        for(Player playerDamage : damagers){
            int pointsIndex = player.getSkulls() + damagers.indexOf(playerDamage);
            if(match.getFinalFrenzy()){
                pointsIndex = pointsIndex - player.getSkulls();
                playerDamage.addPoints(BOARD_FLIPPED_POINTS[pointsIndex]);
            }
            else if(pointsIndex < DAMAGE_POINTS.length)
                playerDamage.addPoints(DAMAGE_POINTS[pointsIndex]);
            else
                playerDamage.addPoints(LOWEST_DAMAGE_POINTS);
        }
    }

    /**
     * Verifies if someone has scored a multikill and gives him the correct amount of points
     * @param player
     */
    private void verifyMultikills(Player player){
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
        if(player.getOverkill()!=null)
            killer.add(player.getOverkill());
        match.getGameBoard().addKill(killer);

        // Finally, reset the damage, so that the player can play again
        player.resetDamage();

        // Aaaaaand remove him from the board
        player.removeFromBoard();
    }
}
