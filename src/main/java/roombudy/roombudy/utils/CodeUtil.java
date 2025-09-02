package roombudy.roombudy.utils;

import java.security.SecureRandom;

public class CodeUtil {
    private static final SecureRandom RANDOM = new SecureRandom();


    /**
     * 4자리의 랜덤 숫자 인증코드 생성
     * @return String
     */
    public static String createCode() {
        int n = RANDOM.nextInt(10_000); // 0 ~ 9999
        return String.format("%04d", n);
    }

}