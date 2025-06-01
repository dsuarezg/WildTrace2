package org.ironhack.frontendservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/about")
public class AboutController {

    /****
     * Handles HTTP GET requests to "/about/me" and returns the view name for the "about me" page.
     *
     * @return the logical view name "about/me"
     */
    @GetMapping("/me")
    public String aboutMe() {
        return "about/me";
    }
}
