package pl.workreporter.web.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;

import java.security.Principal;

/**
 * Created by Sergiusz on 21.08.2017.
 */
@RestController
public class UserDetailsRestController {
    @RequestMapping("/auth")
    public RestResponse<Principal> user(Principal user) {
        return new RestResponseSuccess<>(user);
    }

}
