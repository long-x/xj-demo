package com.ecdata.cmp.common.utils;


import com.ecdata.cmp.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author xuxinsheng
 * @since 2019-03-30
 */
public final class DateUtil {

    private DateUtil() {

    }

    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    public static String getNowStr() {
        return formatDate(getNow());
    }

    public static String getNowStr(String pattern) {
        return formatDate(getNow(), pattern);
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null || pattern == null) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    public static String formatDate(long timeMillis, String pattern) {
        Date date = new Date(timeMillis);
        return formatDate(date, pattern);
    }

    public static Date parseStr(String dateStr) throws ParseException {
        return parseStr(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseStr(String dateStr, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(dateStr);

    }

    public static Date parseUTCStr(String dateStr) throws ParseException {
        return parseUTCStr(dateStr, "yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

    public static Date parseUTCStr(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.parse(dateStr);

    }

    public static List<String> getDateStrList(String beginTime, String endTime, int type) throws ParseException {
        List<String> list = new ArrayList<>();
        list.add(beginTime);
        String pattern;
        int calendarField;
        if (type == Constants.ONE) {
            pattern = "yyyy";
            calendarField = Calendar.YEAR;
        } else if (type == Constants.TWO) {
            pattern = "yyyy-MM";
            calendarField = Calendar.MONTH;
        } else {
            pattern = "yyyy-MM-dd";
            calendarField = Calendar.DATE;
        }
        Date date = parseStr(beginTime, pattern);
        Date end = parseStr(endTime, pattern);
        final int negative = -1;
        if (date.compareTo(end) > negative) {
            return list;
        }
        Calendar c = Calendar.getInstance();
        while (date.before(end)) {
            c.setTime(date);
            c.add(calendarField, 1);
            date = c.getTime();
            list.add(formatDate(date, pattern));
        }

        if (!list.get(list.size() - 1).equals(endTime)) {
            list.add(endTime);
        }
        return list;
    }

    public static String dealDateFormat(String oldDate) {
        if (StringUtils.isBlank(oldDate)) {
            return null;
        }
        Date date1 = null;
        DateFormat df2 = null;
        try {
            oldDate = oldDate.replace("Z", " UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
            if (oldDate != null && oldDate.length() > 25)
                df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            Date date = df.parse(oldDate);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
            date1 = df1.parse(date.toString());
            df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return df2.format(date1);
    }

}
