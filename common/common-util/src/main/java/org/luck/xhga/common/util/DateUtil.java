package org.luck.xhga.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

/**
 * 日期工具类
 * @author xhga
 */
public class DateUtil {

    public static final DateTimeFormatter Y_M_T_D_S_Z = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            .appendOffsetId().toFormatter();

    public static LocalDateTime stringToLocalDateTime(String dateStr, DateTimeFormatter dateTimeFormatter) {
        if (dateStr == null || dateStr.length() == 0) {
            return null;
        }
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateStr, dateTimeFormatter);
        return offsetDateTime.toLocalDateTime();
    }
    public static String localDateTimeToString(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        if (localDateTime == null) {
            return null;
        }
        ZoneOffset offset = OffsetDateTime.now().getOffset();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(offset);
        return offsetDateTime.format(dateTimeFormatter);
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
    public static LocalDateTime date2localDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 时间间隔-duration
     * desc:    duration.toXXX() 相差XXX数
     * example: duration.toDays() 相差天数(舍去不足24小时的时间段)
     * @param start 开始时间
     * @param end   结束时间
     * @return Duration
     */
    public static Duration between(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new NullPointerException("between: 参数不能为null");
        }
        return Duration.between(start, end);
    }

    /**
     * 天数差
     * @param before 之前时间
     * @param after  之后时间
     * @return 天数
     */
    public static long daysDiff(LocalDateTime before, LocalDateTime after) {
        if (before == null || after == null) {
            throw new NullPointerException("intervalDays: 参数不能为null");
        }
        long beforeDay = before.toEpochSecond(ZoneOffset.ofHours(0)) / 60 / 60 / 24;
        long afterDay = after.toEpochSecond(ZoneOffset.ofHours(0)) / 60 / 60 / 24;
        return afterDay - beforeDay;
    }
    /**
     * 天数差
     * @param before 之前时间
     * @param after  之后时间
     * @return 天数
     */
    public static long daysDiff(LocalDate before, LocalDate after) {
        if (before == null || after == null) {
            throw new NullPointerException("intervalDays: 参数不能为null");
        }
        long beforeDay = before.toEpochDay();
        long afterDay = after.toEpochDay();
        return afterDay - beforeDay;
    }
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-10 19:11:11", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime dateTime2 = LocalDateTime.parse("2020-06-10 10:11:11", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        long l = daysDiff(dateTime.toLocalDate(), dateTime2.toLocalDate());
        System.out.println(l);
    }
}