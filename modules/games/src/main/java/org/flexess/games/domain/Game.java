package org.flexess.games.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String playerWhite;

    private String playerBlack;

    private Character activeColour;

    private String fen;

    private int fullMoveNumber;

    private GameStatus status;

    private Date created;

    private Date modified;

    public Game() {
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

    public Long getId() {
        return id;
    }

    public String getPlayerWhite() {
        return playerWhite;
    }

    public void setPlayerWhite(String playerWhite) {
        this.playerWhite = playerWhite;
    }

    public String getPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(String playerBlack) {
        this.playerBlack = playerBlack;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public Character getActiveColour() {
        return activeColour;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public void setFullMoveNumber(int fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
    }

    public void setActiveColour(Character activeColour) {
        this.activeColour = activeColour;
    }

    // see https://www.firstfewlines.com/post/spring-boot-json-format-date-using-jsonserialize-and-jsonformat/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Game #" +id;
    }
}
