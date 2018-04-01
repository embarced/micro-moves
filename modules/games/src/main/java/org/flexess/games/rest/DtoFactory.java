package org.flexess.games.rest;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.Move;

/**
 * Factory fpr the Data Transfer Objects.
 */
class DtoFactory {

    private DtoFactory() {
    }

    static GameInfoDto createGameInfoDto(Game game) {
        GameInfoDto info = new GameInfoDto();
        info.setGameId(game.getId());
        info.setPlayerWhite(game.getPlayerWhite());
        info.setPlayerBlack(game.getPlayerBlack());
        info.setStatus(game.getStatus());
        return info;
    }

    static GameDetailsDto createGameDetailsDto(Game game) {
        GameDetailsDto details = new GameDetailsDto();
        details.setGameId(game.getId());
        details.setActiveColour(game.getActiveColour());
        details.setCreated(game.getCreated());
        details.setModified(game.getModified());
        details.setFen(game.getPosition());
        details.setFullMoveNumber(game.getFullMoveNumber());
        details.setPlayerBlack(game.getPlayerBlack());
        details.setPlayerWhite(game.getPlayerWhite());
        details.setStatus(game.getStatus());
        return details;
    }

    static MoveDto createMoveDto(Move move) {
        MoveDto mDto = new MoveDto();
        mDto.setMoveId(move.getId());
        mDto.setNumber(move.getNumber());
        mDto.setText(move.getText());
        mDto.setCreated(move.getCreated());
        return mDto;
    }

}
