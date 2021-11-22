package com.ecdata.cmp.iaas.utils;

        import com.ecdata.cmp.iaas.mapper.IaasDayTimesMapper;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.List;

/**
 * @title: 天数工具类
 * @Author: shig
 * @description: 根据天数生成日期
 * @Date: 2020/2/21 2:11 下午
 */
public class DayUtils {

    private static Date getDateAdd(int days) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days+1);
        return c.getTime();
    }

    public static List<String> getDaysBetwwen(int days) {
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        Long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24l;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            System.out.println(df.format(d));
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }

    public static void main(String[] args) {

        DayUtils dayUtils=new DayUtils();
        System.out.println(dayUtils.getDaysBetwwen(7));
    }
}