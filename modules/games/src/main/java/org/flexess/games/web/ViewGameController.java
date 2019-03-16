package org.flexess.games.web;

import org.flexess.games.domain.Game;
import org.flexess.games.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ViewGameController {

    private GameService gameService;

    public ViewGameController(@Autowired GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * View a single game by ID.
     *
     * @param model Spring Web MVC model for template
     * @param id ID of the game
     * @return template name
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(ModelMap model, @PathVariable("id") Long id) {

        Game game = gameService.getGameById(id);
        model.addAttribute("game", game);

        return "view_game";
    }
}
