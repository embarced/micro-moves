package org.flexess.games.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Persistent game. Contains infos about players, game state (e.g. position).
 * Root aggregate, contains moves.
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        modified();
    }

    public Character getActiveColour() {
        return activeColour;
    }

    public void setActiveColour(Character activeColour) {
        this.activeColour = activeColour;
        modified();
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void setFullMoveNumber(int fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
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

    @Override
    public String toString() {
        return "Game #" +id;
    }
}
