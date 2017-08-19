package pl.workreporter.web.service.pwdgen;

import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Created by Sergiusz on 19.08.2017.
 */
@Service
public class PasswordGenerator {
    private final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public String generate(int length) {
        StringBuilder pwdBuilder = new StringBuilder();
        Random ran = new Random();

        for (int i = 0; i < length; i++) {
            pwdBuilder.append(alphabet.charAt(ran.nextInt(alphabet.length())));
        }
        return pwdBuilder.toString();
    }
}
