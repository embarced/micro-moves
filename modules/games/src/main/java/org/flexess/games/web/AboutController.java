package org.flexess.games.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AboutController {

    /**
     * About this module.
     *
     * @return template name
     */
    @RequestMapping(value = "/about.html", method = RequestMethod.GET)
    public String about(ModelMap model) {
        return "about";
    }
}
