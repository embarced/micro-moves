package org.flexess.games.web;

import org.flexess.games.domain.Game;
import org.flexess.games.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class NewGameController {

    @Autowired
    GameService gameService;

    @RequestMapping(value = "/new_game", method = RequestMethod.GET)
    public String showNewGameForm(NewGameForm newGameForm) {
        return "new_game";
    }

    @PostMapping("/new_game")
    public String checkNewGameForm(@Valid NewGameForm newGameForm, BindingResult bindingResult, ModelMap model) {

        if (bindingResult.hasErrors()) {
            return "new_game";
        }

        Game game = gameService.openGame(newGameForm.getPlayerWhite(), 'w');
        gameService.enterGame(game.getId(), newGameForm.getPlayerBlack());

        model.addAttribute("game", game);

        return "new_game_created";
    }

}
