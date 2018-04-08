package org.flexess.games.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.flexess.games.domain.GameStatus;

import java.util.Date;

/**
 * Data transfer object (PofEAA, page 401) for detailed game data.
 *
 * @author stefanz
 */
@JsonPropertyOrder({"gameId", "playerWhite", "playerBlack", "status", "result", "activeColour", "activePlayer", "fen", "created", "modified"})
public class GameDetailsDto extends GameInfoDto {

    private Character activeColour;

    private String result;

    private String fen;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getActivePlayer() {
        String activePlayer = null;
        if (getStatus() == GameStatus.RUNNING) {
            switch (getActiveColour()) {
                case 'w':
                    activePlayer = getPlayerWhite();
                    break;
                case 'b':
                    activePlayer = getPlayerBlack();
                    break;
            }
        }
        return activePlayer;
    }
}
