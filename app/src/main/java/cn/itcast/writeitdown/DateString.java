package cn.itcast.writeitdown;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class DateString {
    private static String mYear;
    private static String mMonth;
    private static String mDay;

    public static String StringData(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mYear + "-" + mMonth + "-" + mDay;
    }
    public static int time(String time){
        String[] split=time.split("-");
        return 365*Integer.parseInt(split[0])+30*Integer.parseInt(split[1])+Integer.parseInt(split[2]);
    }
    public static int picker(String time){
        String[] split=time.split("-");
        return 24*60*(365*Integer.parseInt(split[0])+30*Integer.parseInt(split[1])+Integer.parseInt(split[2]))+60*Integer.parseInt(split[3])+Integer.parseInt(split[4]);
    }
    public static boolean deadline(String now, String deadline){

        return time(now) >=time(deadline);
    }
}

