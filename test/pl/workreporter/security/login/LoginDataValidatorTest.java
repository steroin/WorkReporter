package pl.workreporter.security.authentication;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Sergiusz on 09.08.2017.
 */
public class LoginDataValidatorTest {
    @Test
    public void validateLogin() throws Exception {
        LoginDataValidationSettings settings = new LoginDataValidationSettings(5, 32, 5, 32);
        LoginDataValidator validator = new LoginDataValidator(settings);
        Assert.assertTrue(validator.validateLogin("authentication"));
        Assert.assertTrue(validator.validateLogin("my-authentication"));
        Assert.assertTrue(validator.validateLogin("-authentication-my"));
        Assert.assertTrue(validator.validateLogin("-authentication-my-"));
        Assert.assertTrue(validator.validateLogin("my_login"));
        Assert.assertTrue(validator.validateLogin("my.authentication"));
        Assert.assertTrue(validator.validateLogin("my$authentication"));

        Assert.assertFalse(validator.validateLogin(""));
        Assert.assertFalse(validator.validateLogin("      "));
        Assert.assertFalse(validator.validateLogin("log"));
        Assert.assertFalse(validator.validateLogin("my authentication"));
        Assert.assertFalse(validator.validateLogin("my@authentication.pl"));
        Assert.assertFalse(validator.validateLogin("my'authentication'"));
        Assert.assertFalse(validator.validateLogin("my`authentication`"));
        Assert.assertFalse(validator.validateLogin("my~authentication"));
        Assert.assertFalse(validator.validateLogin("my~authentication"));
        Assert.assertFalse(validator.validateLogin("/my/authentication"));
        Assert.assertFalse(validator.validateLogin("\\authentication"));
        Assert.assertFalse(validator.validateLogin("myloginmyloginmyloginmyloginmylogin"));
    }

    @Test
    public void validatePassword() throws Exception {
        LoginDataValidationSettings settings = new LoginDataValidationSettings(3, 32, 5, 32);
        LoginDataValidator validator = new LoginDataValidator(settings);
        Assert.assertTrue(validator.validatePassword("password"));
        Assert.assertTrue(validator.validatePassword("PaSSWoRd"));
        Assert.assertTrue(validator.validatePassword("1223password"));
        Assert.assertTrue(validator.validatePassword("password1232103"));
        Assert.assertTrue(validator.validatePassword("pass-word"));
        Assert.assertTrue(validator.validatePassword("password!@#$%^&*"));
        Assert.assertTrue(validator.validatePassword("Psdam029wasmSa@#$##$kd"));

        Assert.assertFalse(validator.validatePassword(""));
        Assert.assertFalse(validator.validatePassword("    "));
        Assert.assertFalse(validator.validatePassword("pwd"));
        Assert.assertFalse(validator.validatePassword(" password"));
        Assert.assertFalse(validator.validatePassword("password "));
        Assert.assertFalse(validator.validatePassword("pass'word"));
        Assert.assertFalse(validator.validatePassword("\\password"));
        Assert.assertFalse(validator.validatePassword("password/"));
        Assert.assertFalse(validator.validatePassword("passwordpasswordpasswordpasswordpassword"));
    }

    @Test
    public void isEmail() throws Exception {
        LoginDataValidationSettings settings = new LoginDataValidationSettings(3, 32, 5, 32);
        LoginDataValidator validator = new LoginDataValidator(settings);

        Assert.assertTrue(validator.isEmail("myemail@domain.com"));
        Assert.assertTrue(validator.isEmail("myemail@domain.domain.com"));
        Assert.assertTrue(validator.isEmail("myemail-myemail@domain.com"));

        Assert.assertFalse(validator.isEmail(""));
        Assert.assertFalse(validator.isEmail("    "));
        Assert.assertFalse(validator.isEmail("@domain.com"));
        Assert.assertFalse(validator.isEmail("@.com"));
        Assert.assertFalse(validator.isEmail("email.com"));
        Assert.assertFalse(validator.isEmail("email com"));
        Assert.assertFalse(validator.isEmail("email-email.com"));
    }
}
