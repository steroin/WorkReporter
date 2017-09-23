package pl.workreporter.web.controllers.user;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.workreporter.security.authentication.CompleteUserDetails;
import pl.workreporter.web.beans.entities.user.User;
import pl.workreporter.web.beans.entities.user.UserDao;
import pl.workreporter.web.beans.entities.user.UserDaoWrapper;
import pl.workreporter.web.beans.security.rest.RestResponse;
import pl.workreporter.web.beans.security.rest.RestResponseSuccess;
import pl.workreporter.web.beans.security.rest.views.user.JsonDataView;
import pl.workreporter.web.service.password.changer.PasswordChanger;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

/**
 * Created by Sergiusz on 23.08.2017.
 */
@RestController
public class UserDataRestController {
    @Autowired
    private UserDaoWrapper userDaoWrapper;
    @Autowired
    private PasswordChanger passwordChanger;

    @RequestMapping(value = "/users/{id}", method = GET)
    @ResponseBody
    @JsonView(JsonDataView.User.class)
    public RestResponse<User> getUser(@PathVariable("id") long userId) {
        return userDaoWrapper.getUserById(userId);
    }

    @RequestMapping(value = "/users/me", method = GET)
    @ResponseBody
    @JsonView(JsonDataView.Myself.class)
    public RestResponse<Map<String, Object>> getMyUserData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CompleteUserDetails cud = null;
        if (principal instanceof CompleteUserDetails) {
            cud = (CompleteUserDetails) principal;
        }
        long userId = cud.getUserId();
        Map<String, Object> map = new HashMap<>();
        User user = userDaoWrapper.getUserById(userId).getResponse();
        map.put("user", user);
        map.put("solution", user.getSolution());
        return new RestResponseSuccess<>(map);
    }

    @RequestMapping(value = "/pwd/{id}", method = PATCH)
    public RestResponse<Integer> changePassword(@PathVariable("id") long userId, @RequestBody Map<String, String> passwords) {
        String password = passwords.get("password");
        String passwordRepeat = passwords.get("newPassword");
        String newPassword = passwords.get("passwordRepeat");
        return new RestResponseSuccess<>(passwordChanger.changePassword(userId, password, passwordRepeat, newPassword).getResultCode());
    }

    @RequestMapping(value = "/users/{id}", method = PATCH)
    @ResponseBody
    public RestResponse<User> updateUserData(@PathVariable("id") long userId, @RequestBody Map<String, String> map) {
        return userDaoWrapper.updateUser(userId, map);
    }
}
