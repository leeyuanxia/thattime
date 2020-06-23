package demo.zcgc.com.thattime.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;

public class AlarmReceiver extends BroadcastReceiver {

    private String dataString;
    private Context ct;
    private NotificationManager notificationManager;
    private Notification notification;
    private MediaPlayer mMediaPlayer;
    private int code;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase database;
    private String remindDay="";
    private String remind_title;
    public static String path = "/data/data/demo.zcgc.com.thattime/databases/ItemDetails.db";
    private int i=4;
    private String selected_day;
    private String tomorrowDate;

    @Override
    public void onReceive(Context context, Intent intent) {
        code = intent.getIntExtra("requestcode", 0);
        startAlertActivity(context);
    }

    private void startAlertActivity(Context context) {
        ct = context;
         Toast.makeText(context, "显示广播", Toast.LENGTH_LONG).show();

       /* if (code==5)
        {
            initDatabase();
            //showNotifi(code);
        }
        else {*/
            showNotifi(code);
        //}
    }
   /* protected SQLiteDatabase getWritable(String path) {
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null);
        return sqLiteDatabase;
    }*/
    /*private String getTomorrowDate(String selectDay){

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
    private String GetSystemDate() {
        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
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
    private void initDatabase() {
        Log.i("广播里的日志--->","开始查找数据库");
        myDatabaseHelper = new MyDatabaseHelper(ct, "ItemDetails.db", null, 1);
        database=myDatabaseHelper.getWritableDatabase();
        *//*database = getWritable(path);*//*
        if (database == null) {
            Log.i("广播里的日志--->","没找到数据库");
            Toast.makeText(ct, "没找到数据库", Toast.LENGTH_LONG).show();
        } else{
            Log.i("广播里的日志--->","找到了数据库");
            Toast.makeText(ct, "数据库查询开始！！！", Toast.LENGTH_LONG).show();}

        Cursor cursor=database.query("ItemDetails",new String[]{"*"},"remind_day=?",new String[]{GetSystemDate()},null,null,null);
        if (cursor.getCount()!=0) {
            while (cursor.moveToNext()) {
                remind_title = cursor.getString(cursor.getColumnIndex("title_des"));
                Log.i("---------->", remind_title);
                Toast.makeText(ct, remind_title+"......."+selected_day, Toast.LENGTH_LONG).show();
                selected_day=cursor.getString(cursor.getColumnIndex("selected_day"));
                shake();
                if (getTomorrowDate(GetSystemDate()).equals(selected_day)) {
                    notificationManager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
                    notification = new Notification.Builder(ct).setContentText("明天是"+remind_title+"的日子，请记得哦！").setContentTitle("纪念日提醒").setSmallIcon(R.drawable.monkey2).setWhen(System.currentTimeMillis()).build();
                    notificationManager.notify(i++, notification);
                }else if (selected_day.equals(CalDayUtil.dataString)){
                    notificationManager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
                    notification = new Notification.Builder(ct).setContentText("今天是" + remind_title + "的日子，好好庆祝下吧！").setContentTitle("纪念日提醒").setSmallIcon(R.drawable.monkey2).setWhen(System.currentTimeMillis()).build();
                    notificationManager.notify(i++, notification);
                }
            }
            database.close();
        }
    }*/

    private void shake() {
        Vibrator vibrator = (Vibrator) ct.getSystemService(Context.VIBRATOR_SERVICE);
        //数组的a[0]表示静止的时间，a[1]代表的是震动的时间，
        long[] time = {0, 500};
        //震动的毫秒值
        //vibrate的第二参数表示从哪里开始循环
        vibrator.vibrate(time, -1);
        mMediaPlayer = MediaPlayer.create(ct, R.raw.remind);
        mMediaPlayer.start();
    }

    private void showNotifi(int requestCode) {
       shake();
        switch (requestCode) {
            case 1:
                notificationManager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
                notification = new Notification.Builder(ct).setContentText("美好的一天，请勤喝水哦！").setContentTitle("8:30健康提醒通知").setSmallIcon(R.drawable.monkey2).setWhen(System.currentTimeMillis()).build();
                notificationManager.notify(1, notification);
                break;
            case 2:
                notificationManager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
                notification = new Notification.Builder(ct).setContentText("下午1:30后进行20-30分钟午睡最佳哦！").setContentTitle("13:30健康提醒通知").setSmallIcon(R.drawable.monkey2).setWhen(System.currentTimeMillis()).build();
                notificationManager.notify(2, notification);
                break;
            case 3:
                notificationManager = (NotificationManager) ct.getSystemService(Context.NOTIFICATION_SERVICE);
                notification = new Notification.Builder(ct).setContentText("早睡早起身体好！晚安！").setContentTitle("22:30健康提醒通知").setSmallIcon(R.drawable.monkey2).setWhen(System.currentTimeMillis()).build();
                notificationManager.notify(3, notification);
                break;
        }
    }


}
