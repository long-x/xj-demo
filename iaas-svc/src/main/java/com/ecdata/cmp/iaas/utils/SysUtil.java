package com.ecdata.cmp.iaas.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**系统基本公共方法
 * Created by dong on 2018/3/17.
 */
public class SysUtil {
    /**
     * 去除重复的号码
     * @param list
     * @return
     */
    public static List<Map<String, Object>> removeDuplicate(List<Map<String, Object>> list,String key)
    {

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).get(key).toString().equals(list.get(i).get(key).toString())) {
                    list.remove(j);
                }
            }
        }
        return  list;
    }

    /**
     * 去除重复字符串数组
     * @param sArray
     * @return
     */
    public static String[] removeDuplicate(String[] sArray)
    {
        if (sArray == null)
            return null;

        Set set = new TreeSet();
        for (int i = 0; i < sArray.length; i++) {
            set.add(sArray[i]);
        }
        return  (String[]) set.toArray(new String[0]);

    }

    /**
     * 去除重复字符串
     * @param str 如  a,b,c,d
     * @param regex  如 ，
     * @return
     */
    public static String removeDuplicate(String str,String regex)
    {
        if (str == null)
            return null;
        String[] sArray = str.split(regex);
        sArray = removeDuplicate(sArray);
        String s = null;
        for (int i = 0; i < sArray.length; i++) {
            if (i == 0)
                s = sArray[i];
            else
                s = s + regex + sArray[i];
        }
        return  s;

    }

    public static List<String> getAllSameElement(String[] strArr1,String[] strArr2) {
        if(strArr1 == null || strArr2 == null) {
            return null;
        }
        Arrays.sort(strArr1);
        Arrays.sort(strArr2);

        List<String> list = new ArrayList<String>();

        int k = 0;
        int j = 0;
        while(k<strArr1.length && j<strArr2.length) {
            if(strArr1[k].compareTo(strArr2[j])==0) {
                if(strArr1[k].equals(strArr2[j]) ) {
                    list.add(strArr1[k]);
                    k++;
                    j++;
                }
                continue;
            } else  if(strArr1[k].compareTo(strArr2[j])<0){
                k++;
            } else {
                j++;
            }
        }
        return list;
    }

    public static Map<String, Object> getMapFromList(List<Map<String, Object>> list,String key, String value) {
        if (list == null || key== null || value == null )
            return null;
        Map resultMap = null;
        for (int i = 0; i < list.size(); i++) {
            Map tempMap = list.get(i);
            String tempValue = tempMap.get(key)==null?"":tempMap.get(key).toString();
            if (value.equalsIgnoreCase(tempValue))
                resultMap = tempMap;

        }
        return  resultMap;
    }

    public static int getMaxValue(List<Map<String, Object>> list,String key) {
        if (list == null)
            return 0;
        int maxValue = 0;
        for (int i = 0; i < list.size(); i++) {
            Map tempMap = list.get(i);
            int tempValue = tempMap.get(key)==null?0:Integer.parseInt(tempMap.get(key).toString());
            if (tempValue > maxValue )
                maxValue = tempValue;
        }
        return  maxValue;
    }

    /**
     * 获取日期字符串
     * @param date
     * @param format
     * @return
     */
    public static String getDateStr(Date date,String format)
    {
        Date tempDate = new Date();
        String tempFormat = "yyyy-MM-dd";
        if (date != null)
            tempDate = date;
        if (format != null)
            tempFormat = format;
        SimpleDateFormat df = new SimpleDateFormat(tempFormat);
        return  df.format(tempDate);
    }

    public static String getDateStrByDays(String dateStr,String format,int days)
    {
        String tempFormat = "yyyy-MM-dd";
        if (format != null)
            tempFormat = format;
        SimpleDateFormat df = new SimpleDateFormat(tempFormat);
        Date tempDate = null;
        try {
            tempDate = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        return  getDateStrByDays(tempDate,tempFormat,days);
    }

    /**
     * 根据格式和天数，获取距当前日期相差days天数的日期
     * @param format
     * @param days
     * @return
     */
    public static String getDateStrByDays(Date date,String format,int days)
    {
        Calendar c = Calendar.getInstance();
        if (date == null)
            c.setTime(new Date());
        else
            c.setTime(date);
        c.add(Calendar.DATE, days);
        Date d = c.getTime();
        return  getDateStr(d,format);
    }

    /**
     * 相差天数，开始串和结束串不能都为空
     * @param beginDateStr，如果为空，则默认当前日期
     * @param endDateStr 如果为空，则默认当前日期
     * @param format
     * @return
     */
    public static int getDaysBetween(String beginDateStr,String endDateStr,String format)
    {
        if (beginDateStr == null && endDateStr == null)
            return -1;
        if (format == null)
            format = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            Date beginDate = null;
            if (beginDateStr == null)
                beginDate = new Date();
            else
                beginDate = simpleDateFormat.parse(beginDateStr);

            Date endDate = null;
            if (endDateStr == null)
                endDate = new Date();
            else
                endDate = simpleDateFormat.parse(endDateStr);
            int days = (int) ((endDate.getTime() - beginDate.getTime()) / (1000*3600*24));
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取文件名里的年月串，比如“嫌疑人信息-20190223120029”获取201902
     * @param fileName
     * @return
     */
    public static  String getYMStrByFileName(String fileName,String format)
    {
        if (fileName == null || fileName.length() == 0)
            return null;
        int benginindex = fileName.lastIndexOf("-");
        int endindex = fileName.lastIndexOf(".");
        if (benginindex ==-1 || endindex == -1)
            return  null;
        if (format == null)
            format = "yyyyMMddHHmmss";

        String dateStr = fileName.substring(benginindex+1,endindex);
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            Date date=df.parse(dateStr);
            return fileName.substring(benginindex+1,benginindex+7);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String args[]) throws Exception
    {
        String fileName = "BA111_IA2222";
        String path = "d:";
        //String content = "13391011778,13391011779,13391011780,13391011775";
        //String s = getYMStrByFileName("嫌疑人信息-20190223120502.xls","yyyyMMdd");
        //int s = getDaysBetween("2015-1-1 1:21:29","2015-1-2 1:21:28","yyyy-MM-dd HH:mm:ss");
        String s = getDateStrByDays("2019-01-02 23:23:23",null ,1);
        System.out.println(s);
        //readFromMDExcel("美达","d:","xlsx");
        //readFromDDExcel("滴滴","d:","xlsx");
    }
}
