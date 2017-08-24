package pl.workreporter.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.workreporter.web.beans.security.login.LoginDao;

import java.security.Security;

/**
 * Created by Sergiusz on 08.08.2017.
 */
public class LoginAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private LoginDao loginDao;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        LoginDataValidationSettings settings = new LoginDataValidationSettings(5, 32, 5, 32);
        LoginDataValidator validator = new LoginDataValidator(settings);

        if (!validator.validateLogin(login) || !validator.validatePassword(password)) {
            return null;
        }

        CompleteUserDetails cud;
        if (validator.isEmail(login)) {
            cud = loginDao.loadUserDetailsByEmail(login);
        } else {
            cud = loginDao.loadUserDetails(login);
        }

        if (cud != null && cud.getPassword() != null && BCrypt.checkpw(password, cud.getPassword())) {
            return new UsernamePasswordAuthenticationToken(cud, password, cud.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
