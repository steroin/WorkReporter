package pl.workreporter.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Created by Sergiusz on 21.08.2017.
 */
@RestController
public class UserDetailsRestController {
    @RequestMapping("/auth")
    public Principal user(Principal user) {
        return user;
    }

}
