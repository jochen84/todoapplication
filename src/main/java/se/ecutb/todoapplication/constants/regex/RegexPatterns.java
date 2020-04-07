package se.ecutb.todoapplication.constants.regex;

public class RegexPatterns {

    public static final String PASSWORD_PATTERN = "^(?=.*\\d).{4,8}$";
    public static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    public static final String DATE_PATTERN = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
}
