package xyz.cotoha.program.reminchan.reminder;

import com.joestelmach.natty.*;

import java.util.Date;
import java.util.List;

public class DateParser {

    public static Date parseStringToDate(String text) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(text);
        if (groups.isEmpty()) return null; // 解析できなかった場合はnullを返す

        DateGroup group = groups.get(0);
        List<Date> dates = group.getDates();
        if (!dates.isEmpty()) {
            return dates.get(0); // 最初に見つかった日時を返す
        }

        return null; // 日時が見つからなかった場合はnullを返す
    }
}
