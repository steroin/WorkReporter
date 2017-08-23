package pl.workreporter.web.controllers.messages;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sergiusz on 23.08.2017.
 */
@Controller
public class MessagesController {
    @RequestMapping("/messages")
    public String index() {
        return "messages/index";
    }
}