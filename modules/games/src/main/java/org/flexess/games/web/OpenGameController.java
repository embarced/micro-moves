package org.flexess.games.web;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.GameStatus;
import org.flexess.games.service.GameService;
import org.flexess.games.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
public class OpenGameController {

    private GameService gameService;

    public OpenGameController(@Autowired GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/open_game", method = RequestMethod.GET)
    public String openStart(ModelMap model, @RequestAttribute User user, HttpServletResponse response) {

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "";
        }

        return "open_game";
    }

    @PostMapping("/open_game")
    public String openFinish(@Valid OpenGameForm openGameForm, ModelMap model, @RequestAttribute User user, HttpServletResponse response) {

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "";
        }

        Game game = gameService.openGame(user.getUserid(), openGameForm.getColourAsCharacter());
        if (openGameForm.isComputerOpponent()) {
            gameService.enterGame(game.getId(), "stockfish");
        }

        model.addAttribute("game", game);
        return "game_opened";
    }
}
