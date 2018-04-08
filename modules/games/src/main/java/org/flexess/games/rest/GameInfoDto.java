package org.flexess.games.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.flexess.games.domain.GameStatus;

/**
 * Data transfer object (PofEAA, page 401) for essential game data.
 *
 * @author stefanz
 */
@JsonPropertyOrder({"gameId", "playerWhite", "playerBlack", "status"})
public class GameInfoDto {

    private Long gameId;

    private String playerWhite;

    private String playerBlack;

    private GameStatus status;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPlayerWhite() {
        return playerWhite;
    }

    public void setPlayerWhite(String playerWhite) {
        this.playerWhite = playerWhite;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
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
