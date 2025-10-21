package roombuddy.roombuddy.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 날짜 포멧 유틸 클래스
 */
public class DateFormatUtil {
    public static String DateFormat(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        return dateTime.format(formatter);
    }
}
