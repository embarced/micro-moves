package org.flexess.games.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NewGameForm {

    @NotNull
    @Size(min=2, max=30)
    private String playerWhite;

    @NotNull
    @Size(min=2, max=30)
    private String playerBlack;

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
}
