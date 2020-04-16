package org.feuyeux.rsocket.utils;

import org.junit.Test;

public class TestTokenUtils {
    @Test
    public void test() {
        String userToken = TokenUtils.generate("user", "USER");
        String adminToken = TokenUtils.generate("admin", "ADMIN");
        System.out.println();
        System.out.println("Generated Tokens");
        System.out.println("================");
        System.out.println("Admin: \n" + adminToken);
        System.out.println();
        System.out.println("User: \n" + userToken);
        System.out.println();
    }
}
