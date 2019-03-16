package org.flexess.games.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Persistent game. Contains information about players, game state (e.g. position), status (e.g. RUNNING).
 * Game is a root aggregate, it contains the moves.
 *
 * @author stefanz
 */
@Entity
public class Game {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerWhite;
    private String playerBlack;

    @Column(length = 1)
    private Character activeColour;
    private String position;
    private GameStatus status;
    private GameResult result;

    private final Date created;
    private Date modified;

    /**
     * Default Constructor.
     */
    public Game() {
        this.created = new Date();
        modified();
    }

    public Long getId() {
        return id;
    }

    public String getPlayerWhite() {
        return playerWhite;
    }

    public void setPlayerWhite(String playerWhite) {
        this.playerWhite = playerWhite;
        modified();
    }

    public String getPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(String playerBlack) {
        this.playerBlack = playerBlack;
        modified();
    }

    /**
     * Returns the current game situation.
     *
     * @return Position in FEN format.
     */
    public String getPosition() {
        return position;
    }

    /**
     * Sets the current game situation.
     *
     * @param position Position in FEN format.
     */
    public void setPosition(String position) {
        this.position = position;
        modified();
    }

    /**
     * Active colour, black or white.
     *
     * @return 'b' for black, 'w' for white.
     */
    public Character getActiveColour() {
        return activeColour;
    }

    public String getActivePlayer() {
        String activePlayer = null;
        switch (getActiveColour()) {
            case 'w':
                activePlayer = getPlayerWhite();
                break;
            case 'b':
                activePlayer = getPlayerBlack();
                break;
        }
        return activePlayer;
    }

    /**
     * Checks whether a given user takes part in this game.
     *
     * @param playerToTest user ID of the player to test
     * @return true, if the player takes part in this game
     */
    public boolean isTakingPart(String playerToTest) {
        return playerWhite.equals(playerToTest) || playerBlack.equals(playerToTest);
    }


    /**
     * Set the active colour, black or white.
     *
     * @param activeColour 'b' for black, 'w' for white.
     */
    public void setActiveColour(Character activeColour) {
        this.activeColour = activeColour;
        modified();
    }

    /**
     * Game status, e.g. OPEN, RUNNING ...
     *
     * @return the game status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Set the game status, e.g. OPEN, RUNNING ...
     *
     * @param status the game status
     */
    public void setStatus(GameStatus status) {
        this.status = status;
        modified();
    }

    /**
     * The game result, if game ended.
     *
     * @return the result, or null if not ended yet
     */
    public GameResult getResult() {
        return result;
    }


    /**
     * Set the game result.
     *
     * @param result the result
     */
    public void setResult(GameResult result) {
        this.result = result;
        modified();
    }

    /**
     * When the game has been created.
     *
     * @return date and time
     */
    public Date getCreated() {
        return created;
    }

    /**
     * When the game has been modifies the last time.
     *
     * @return date and time
     */
    public Date getModified() {
        return modified;
    }

    @Override
    public String toString() {
        String pWhite = (playerWhite != null) ? playerWhite : "???";
        String pBlack = (playerBlack != null) ? playerBlack : "???";
        return "Game #" + id + " (" + pWhite + "-" + pBlack + ")";
    }

    private void modified() {
        this.modified = new Date();
    }

}
