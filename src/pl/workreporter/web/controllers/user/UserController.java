package pl.workreporter.web.controllers.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sergiusz on 23.08.2017.
 */
@Controller
public class UserController {
    @RequestMapping("/user")
    public String userIndex() {
        return "user/index";
    }
}
