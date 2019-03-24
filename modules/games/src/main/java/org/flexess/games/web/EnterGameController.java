package org.flexess.games.web;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.GameStatus;
import org.flexess.games.service.GameService;
import org.flexess.games.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
public class EnterGameController {

    private GameService gameService;

    public EnterGameController(@Autowired GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Enter a game as the second player
     *
     * @param model Spring Web MVC model for template
     * @param id ID of the game
     * @param user logged in User
     * @return template name
     */
    @RequestMapping(value = "/enter", method = RequestMethod.GET)
    public String enterStart(ModelMap model, @RequestParam("game_id") Long id, @RequestAttribute User user, HttpServletResponse response) {

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "";
        }

        Game game = gameService.getGameById(id);
        if (game == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }
        model.addAttribute("game", game);

        if (game.getStatus() == GameStatus.OPEN) {
            if (game.getPlayerBlack() == null) {
                game.setPlayerBlack(user.getUserid());
            } else {
                game.setPlayerWhite(user.getUserid());
            }
            return "enter_game";
        }

        return "view_game";
    }

    @PostMapping("/enter")
    public String enterFinish(@Valid EnterGameForm enterGameForm, ModelMap model, @RequestAttribute User user, HttpServletResponse response) {

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "";
        }

        long id = enterGameForm.getId();
        Game game = gameService.getGameById(id);
        if (game == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "";
        }
        model.addAttribute("game", game);

        if (game.getStatus() == GameStatus.OPEN) {
            gameService.enterGame(id, user.getUserid());
            return "game_entered";
        }

        return "view_game";
    }
}
