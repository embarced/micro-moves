package org.flexess.games.rest;

import org.flexess.games.domain.GameStatus;

import java.util.Date;

public class GameInfo {

    protected Long id;

    protected String playerWhite;

    protected String playerBlack;

    protected GameStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
