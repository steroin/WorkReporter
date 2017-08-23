package pl.workreporter.web.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.workreporter.security.login.CompleteUserDetails;
import pl.workreporter.web.beans.entities.userdata.UserData;
import pl.workreporter.web.beans.entities.userdata.UserDataDao;
import pl.workreporter.web.service.password.changer.PasswordChangeResult;
import pl.workreporter.web.service.password.changer.PasswordChanger;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

/**
 * Created by Sergiusz on 23.08.2017.
 */
@RestController
public class UserDataRestController {
    @Autowired
    private UserDataDao userDataDao;
    @Autowired
    private PasswordChanger passwordChanger;

    @RequestMapping(value = "/users/{id}", method = GET)
    @ResponseBody
    public UserData getUserData(@PathVariable("id") long userId) {
        return userDataDao.getUserData(userId);
    }

    @RequestMapping(value = "/users/me", method = GET)
    @ResponseBody
    public UserData getUserData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompleteUserDetails cud = null;
        if (principal instanceof CompleteUserDetails) {
            cud = (CompleteUserDetails) principal;
        }
        long userId = cud.getUserId();
        return userDataDao.getUserData(userId);
    }

    @RequestMapping(value = "/pwd/{id}", method = PATCH)
    @ResponseBody
    public PasswordChangeResult changePassword(@PathVariable("id") long userId, String password, String passwordRepeat, String newPassword) {
        return passwordChanger.changePassword(userId, password, passwordRepeat, newPassword);
    }

    @RequestMapping(value = "/users/{id}", method = PATCH)
    @ResponseBody
    public void updateUserData(@PathVariable("id") long userId, UserData userData) {
        userDataDao.updateUserData(userData);
    }
}
