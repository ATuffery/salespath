package fr.iutrodez.salespath.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/account")
public class AccountController {

    @GetMapping(value="/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return "login";
    }
}