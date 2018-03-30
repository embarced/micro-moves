package org.flexess.games.rest;

import org.flexess.games.domain.GameStatus;

import java.util.Date;

public class GameDetails extends GameInfo {

    private Character activeColour;

    private String fen;

    private int fullMoveNumber;

    private Date created;

    private Date modified;

    public Character getActiveColour() {
        return activeColour;
    }

    public void setActiveColour(Character activeColour) {
        this.activeColour = activeColour;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
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

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
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
}
