package org.techtown.ideaconcert.RegisterActivityDir;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatePwdEmail {

    public static boolean validatePwd(String password) {
        /*
         * 패스워드 정책은 알파벳, 숫자, 특수문자를 조합하여 8글자 이상으로 설정한다.
         * (?=.{8,})        8자 이상.
         * (?=..*[0-9])     하나 이상의 숫자 포함
         * (?=.*[a-z])      하나 이상의 소문자 포함
         */
        String passwordPolicy = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z]).*$";
        return password.matches(passwordPolicy);
    }

    public static boolean validateEmail(String email) {
        /*
         * 이메일 정책은 알파벳과 숫자를 포함하고 '@'와 '.xxx'를 포함해야한다.
         * [_a-zA-Z0-9-\.]   하나 이상의 알파벳잇나 숫자를 포함한다.
         * @[\.a-zA-Z0-9-]  '@'와 하나 이상의 알파벳이나 숫자를 포함한다.
         * .[a-zA-Z]+$       '.'과 하나 이상의 알파벳을 포함한다.
         */
        String emailPolicy = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(emailPolicy);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
