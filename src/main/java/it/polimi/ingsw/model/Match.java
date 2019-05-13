package it.polimi.ingsw.model;

import java.util.*;

public class Match extends Identifiable{
    /**
     * States whether the match in in final frenzy
     */
    private boolean finalFrenzy;
    /**
     * List containing all the players participating in this match
     */
    private List<Player> players = new ArrayList<>();
    /**
     * The GameBoard associated with this match
     */
    private GameBoard gameBoard;
    /**
     * Contains all the information needed for the current turn
     */
    private Turn currentTurn;
    /**
     * Counts the number of turns
     */
    private int turnsElapsed;

    public Match(List<String> nicknames, BoardType boardType) throws ResourceException {
        if (nicknames.size() < 3) {
            throw new InvalidOperationException("Not enough players");
        }
        if (nicknames.size() > 5) {
            throw new InvalidOperationException("Too many players");
        }

        this.finalFrenzy = false;

        List<PlayerToken> tokens = Arrays.asList(PlayerToken.values());
        Collections.shuffle(tokens);
        Iterator<PlayerToken> token = tokens.iterator();
        nicknames.forEach((nickname) -> players.add(new Player(nickname, token.next())));

        this.gameBoard = new GameBoard(8, boardType);
        this.currentTurn = new Turn(players.get(0), TurnType.FIRST_TURN);
        players.get(0).setStartingPlayer(true);
        this.turnsElapsed = 0;
    }

    /**
     * Standard constructor, generates his id
     */
    Match() throws ResourceException{
        finalFrenzy = false;
        gameBoard = new GameBoard();
    }

    /**
     * Adds a player to the match
     * @param player the player to be added
     */
    public void addPlayer(Player player){
            if(players.size() < 5)
                players.add(player);
            else
                throw new InvalidOperationException("Maximum number of active players reached");
    }

    /**
     * Gives you the list of players currently participating in this match
     * @return The list of players currently participating in this match
     */
    public List<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    public Player getPlayerByNickname(String nickname) {
        for (Player p : players) {
            if (nickname.equals(p.getNickname())) {
                return p;
            }
        }
        throw new InvalidOperationException("Nickname not found");
    }

    /**
     * Sets final frenzy mode to true
     */
    public void activateFinalFrenzy(){
        finalFrenzy = true;
    }

    /**
     * Gives you the gameboard associated with this match
     * @return the gameboard fo the match
     */
    public GameBoard getGameBoard(){
        return gameBoard;
    }

    /**
     * Gives you all the information about this particular turn
     * @return The current turn
     */
    public Turn getCurrentTurn(){
        return currentTurn;
    }

    /**
     * Goes to the next turn
     */
    public void nextTurn(){
        turnsElapsed++;
        int indexNextPlayer = players.indexOf(currentTurn.getCurrentPlayer())+1;
        if (indexNextPlayer == players.size()) {
            indexNextPlayer = 0;
        }
        Player nextPlayer = players.get(indexNextPlayer);

        if (turnsElapsed <= players.size())
            currentTurn = new Turn(nextPlayer, TurnType.FIRST_TURN);
        else if(finalFrenzy)
            currentTurn = new Turn(nextPlayer, TurnType.FINAL_FRENZY);
        else
            currentTurn = new Turn(nextPlayer, TurnType.STANDARD);
    }

    /**
     * This method returns true if there is a sufficient number of active players to play the match. Returns false
     * otherwise.
     * @return Whether the match is active
     */
    public boolean isActive(){
        int activePlayers = 0;

        for(Player player : players)
            if(player.isActive())
                activePlayers++;

        return activePlayers >= 3;
    }
}
