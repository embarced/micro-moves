package org.flexess.games.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Persistent game. Contains information about players, game state (e.g. position).
 * Game is a root aggregate, it contains the moves.
 *
 * @author stefanz
 */
@Entity
public class Game {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String playerWhite;
    private String playerBlack;
    private Character activeColour;
    private String position;
    private int fullMoveNumber;
    private GameStatus status;
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

    /**
     * Set the active colour, black or white.
     *
     * @param activeColour 'b' for black, 'w' for white.
     */
    public void setActiveColour(Character activeColour) {
        this.activeColour = activeColour;
        modified();
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void setFullMoveNumber(int fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
        modified();
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
        modified();
    }

    private void modified() {
        this.modified = new Date();
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    @Override
    public String toString() {
        return "Game #" +id;
    }
}
