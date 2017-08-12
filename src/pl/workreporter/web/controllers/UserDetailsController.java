package pl.workreporter.web.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.workreporter.security.login.CompleteUserDetails;

/**
 * Created by Sergiusz on 12.08.2017.
 */
@ControllerAdvice
public class UserDetailsController {

    @ModelAttribute("userDetails")
    public CompleteUserDetails populateUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof  CompleteUserDetails) {
            return (CompleteUserDetails) principal;
        }
        return null;
    }
}
