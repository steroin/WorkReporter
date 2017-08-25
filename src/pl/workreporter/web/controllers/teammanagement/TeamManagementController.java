package pl.workreporter.web.controllers.teammanagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sergiusz on 25.08.2017.
 */
@Controller
public class TeamManagementController {

    @RequestMapping("/team")
    public String teamManagementIndex() {
        return "team/index";
    }
}
