package org.flexess.games.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    // see https://www.firstfewlines.com/post/spring-boot-json-format-date-using-jsonserialize-and-jsonformat/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    // see https://www.firstfewlines.com/post/spring-boot-json-format-date-using-jsonserialize-and-jsonformat/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
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
