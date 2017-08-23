package pl.workreporter.web.service.password.changer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.workreporter.web.beans.security.login.LoginDao;
import pl.workreporter.web.service.password.validator.PasswordValidator;

/**
 * Created by Sergiusz on 23.08.2017.
 */
@Service
public class PasswordChanger {
    @Autowired
    LoginDao loginDao;
    @Autowired
    PasswordValidator validator;

    public PasswordChangeResult changePassword(long userId, String existingPassword, String existingPasswordRepeat, String newPassword) {
        if (!existingPassword.equals(existingPasswordRepeat)) {
            return PasswordChangeResult.REPEAT_DOESNT_MATCH;
        }
        String hashedExistingPassword = loginDao.getPasswordHash(userId);

        if (!BCrypt.checkpw(existingPassword, hashedExistingPassword)) {
            return PasswordChangeResult.WRONG_EXISTING_PASSWORD;
        }

        if (!validator.validatePassword(newPassword)) {
            return PasswordChangeResult.INVALID_NEW_PASSWORD;
        }

        try {
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(16));
            loginDao.changeUserPassword(userId, hashedNewPassword);
            return PasswordChangeResult.SUCCESS;
        } catch (Exception e) {
            return PasswordChangeResult.UNKNOWN_ERROR;
        }
    }
}
