package demo.zcgc.com.thattime.uitls;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalDayUtil {
    private static  Calendar calendar = Calendar.getInstance();;
    private static int year, month, day;
    public static String dataString;
    private static String formerDate;
    private static String tomorrowDate;


    //str1小日期，str2大日期
    public static long GetDay(String str1, String str2) {
        int y1 = new Integer(str1.substring(0, 4)).intValue();
        int y2 = new Integer(str2.substring(0, 4)).intValue();
        int m1 = new Integer(str1.substring(5, 7)).intValue();
        int m2 = new Integer(str2.substring(5, 7)).intValue();
        int d1 = new Integer(str1.substring(8, 10)).intValue();
        int d2 = new Integer(str2.substring(8, 10)).intValue();
        Calendar c1 = Calendar.getInstance();
        c1.set(y1, m1, d1,0,0,0);
        Calendar c2 = Calendar.getInstance();
        c2.set(y2, m2, d2,0,0,0);
        return (c2.getTime().getTime() - c1.getTime().getTime()) / (86400000);
    }

    /*    public static void main(String[] args)
        {
            String str1="2018年09月01日";
            String str2="2018年09月16日";
            long days=GetDay(str1,str2);
            System.out.println(days);
        }*/
    public static String GetSystemDate() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month + 1 < 10) {
            if (day < 10) {
                dataString = year + "年" + "0" + (month + 1) + "月" + "0" + day + "日";
            } else {
                dataString = year + "年" + "0" + (month + 1) + "月" + day + "日";
            }

        } else {
            if (day < 10) {
                dataString = year + "年" + (month + 1) + "月" + "0" + day + "日";
            } else {
                dataString = year + "年" + (month + 1) + "月" + day + "日";
            }
        }
        return dataString;
    }
    public static String getformerDate(String selectDay){

        int y = new Integer(selectDay.substring(0, 4)).intValue();
        int m = new Integer(selectDay.substring(5, 7)).intValue();
        int d = new Integer(selectDay.substring(8, 10)).intValue();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(y,m-1,d);
        calendar2.setTimeInMillis(calendar2.getTimeInMillis()-1000*60*60*24-1);
          //得到前一天
        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
        if (month2 + 1 < 10) {
            if (day2 < 10) {
                formerDate = year2 + "年" + "0" + (month2 + 1) + "月" + "0" + day2 + "日";
            } else {
                formerDate = year2+ "年" + "0" + (month2 + 1) + "月" + day2 + "日";
            }

        } else {
            if (day2 < 10) {
                formerDate = year2 + "年" + (month2 + 1) + "月" + "0" + day2 + "日";
            } else {
                formerDate = year2 + "年" + (month2 + 1) + "月" + day2 + "日";
            }
        }

        return formerDate;
    }
    public static String getTomorrowDate(String selectDay){

        int y = new Integer(selectDay.substring(0, 4)).intValue();
        int m = new Integer(selectDay.substring(5, 7)).intValue();
        int d = new Integer(selectDay.substring(8, 10)).intValue();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(y,m-1,d);
        calendar2.setTimeInMillis(calendar2.getTimeInMillis()+1000*60*60*24-1);
        //得到前一天
        int year2 = calendar2.get(Calendar.YEAR);
        int month2 = calendar2.get(Calendar.MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
        if (month2 + 1 < 10) {
            if (day2 < 10) {
                tomorrowDate = year2 + "年" + "0" + (month2 + 1) + "月" + "0" + day2 + "日";
            } else {
                tomorrowDate = year2+ "年" + "0" + (month2 + 1) + "月" + day2 + "日";
            }

        } else {
            if (day2 < 10) {
                tomorrowDate = year2 + "年" + (month2 + 1) + "月" + "0" + day2 + "日";
            } else {
                tomorrowDate = year2 + "年" + (month2 + 1) + "月" + day2 + "日";
            }
        }

        return tomorrowDate;
    }
    public static String distance_day(String str2){
        String distance_day="";
        if (CalDayUtil.GetDay(dataString,str2)>0){
            distance_day="还剩"+CalDayUtil.GetDay(dataString,str2)+"天";
        }else if (CalDayUtil.GetDay(dataString,str2)==0){
            distance_day="今天";
        }else
        {
            distance_day="已过"+(Math.abs(CalDayUtil.GetDay(dataString,str2)))+"天";
        }
        return distance_day;
    }
    public static String getCreateTime(){
        long createTime=0;
        createTime=System.currentTimeMillis();
        return String.valueOf(createTime);
    }

    public static String getNextYear(String remind_day) {
        int thisMonth=Integer.parseInt(remind_day.substring(5,7));
        int thisYear=Integer.parseInt(remind_day.substring(0,4));
        int thisDay=Integer.parseInt(remind_day.substring(8,10));
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,thisYear);
        c.set(Calendar.MONTH,thisMonth-1);
        c.set(Calendar.DAY_OF_MONTH,thisDay);
        c.add(Calendar.YEAR,+1);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(c.getTime());
    }

    public static String getNextMonth(String remind_day) {
        int thisMonth=Integer.parseInt(remind_day.substring(5,7));
        int thisYear=Integer.parseInt(remind_day.substring(0,4));
        int thisDay=Integer.parseInt(remind_day.substring(8,10));
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,thisYear);
        c.set(Calendar.MONTH,thisMonth-1);
        c.set(Calendar.DAY_OF_MONTH,thisDay);
        c.add(Calendar.MONTH,+1);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(c.getTime());

    }
}
