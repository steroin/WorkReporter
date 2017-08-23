package pl.workreporter.web.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.web.beans.entities.userdata.UserData;
import pl.workreporter.web.beans.entities.userdata.UserDataDao;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Sergiusz on 23.08.2017.
 */
@RestController
public class UserDataRestController {
    @Autowired
    UserDataDao userDataDao;

    @RequestMapping(value = "/users/{id}", method = GET)
    @ResponseBody
    public UserData getUserData(@PathVariable("id") long userId) {
        return userDataDao.getUserData(userId);
    }
}
