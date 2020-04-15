package org.feuyeux.rsocket.utils;

import org.junit.Test;

public class TestPasswordUtils {
    @Test
    public void test() {
        String pw = PasswordUtils.encode("pw");
        System.out.println(pw);
        boolean match = PasswordUtils.matches("pw", pw);
        System.out.println(match);
    }
}