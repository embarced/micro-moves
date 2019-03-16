package org.flexess.games.web;


import org.flexess.games.domain.Game;
import org.flexess.games.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AllGamesController {

    @Autowired
    GameService gameService;

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public String home(ModelMap model) {

        Iterable<Game> allGames = gameService.getAllGames();
        model.addAttribute("games", allGames);

        return "all_games";
    }
}
