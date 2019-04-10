package org.flexess.games.web;


import org.flexess.games.domain.Game;
import org.flexess.games.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AllGamesController {

    private GameService gameService;

    public AllGamesController(@Autowired GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Lists all games. Acts as the homepage.
     *
     * @param model  Spring Web MVC model for template
     * @return template name
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String allGames(ModelMap model) {

        Iterable<Game> allGames = gameService.getAllGames();
        model.addAttribute("games", allGames);

        return "all_games";
    }
}
