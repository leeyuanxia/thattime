package demo.zcgc.com.thattime.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import demo.zcgc.com.thattime.BroadcastReceiver.AlarmReceiver;
import demo.zcgc.com.thattime.BroadcastReceiver.DayReceiver;
import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.activity.MainActivity;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;



public class DayRemindService extends Service {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase database;
    private String remind_title,selected_day;
    private int i=4;
    private String tomorrowDate,dataString;
    private NotificationManager notificationManager;
    private Notification notification;
    private MediaPlayer mMediaPlayer;
    private int dayId=0;
    private String remind_day;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "day开启", Toast.LENGTH_LONG).show();
        if (intent!=null){
            if (intent.getStringExtra("start_code")!=null)
            {
                if ("Day".equals(intent.getStringExtra("start_code"))) {
                    initDatabase();
                }
            }
        }
        flags= ((int) Service.START_STICKY);

        return super.onStartCommand(intent, flags, startId);
    }

    private void initDatabase() {
        Log.i("DayService里的日志--->","开始查找数据库");
        try {
            myDatabaseHelper = new MyDatabaseHelper(getApplicationContext(), "ItemDetails.db", null, 1);
            database=myDatabaseHelper.getWritableDatabase();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"出现异常",Toast.LENGTH_LONG).show();
        }

        if (database == null) {
            Log.i("DayService里的日志--->","没找到数据库");
            Toast.makeText(getApplicationContext(), "没找到数据库", Toast.LENGTH_LONG).show();
        } else{
            Log.i("DayService里的日志--->","找到了数据库");
            Toast.makeText(getApplicationContext(), "DayService数据库查询开始！！！", Toast.LENGTH_LONG).show();}
        Cursor cursor=database.query("ItemDetails",new String[]{"*"},"remind_day=?",new String[]{GetSystemDate()},null,null,null);
        if (cursor.getCount()!=0) {
            while (cursor.moveToNext()) {
                remind_title = cursor.getString(cursor.getColumnIndex("title_des"));
                Log.i("---------->", remind_title);
                dayId=cursor.getInt(cursor.getColumnIndex("id"));
                selected_day=cursor.getString(cursor.getColumnIndex("selected_day"));
                remind_day=cursor.getString(cursor.getColumnIndex("remind_day"));
                Toast.makeText(getApplicationContext(), remind_title+"......."+selected_day, Toast.LENGTH_LONG).show();
                shake();
                String ifrepeat=cursor.getString(cursor.getColumnIndex("ifrepeat"));
                if (CalDayUtil.getTomorrowDate(GetSystemDate()).equals(selected_day)) {
                    notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);


                   /* Intent appIntent = null;
                    appIntent = new Intent(getApplicationContext(), MainActivity.class);
                    appIntent.setAction(Intent.ACTION_MAIN);
                    appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    appIntent.putExtra("flag", "notifyIntent");
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/
                    notification = new Notification.Builder(getApplicationContext())
                            .setContentTitle("纪念日提醒").setSmallIcon(R.drawable.monkey2).setWhen(System.currentTimeMillis())
                            .setStyle(new Notification.BigTextStyle()
                                    .bigText("明天是"+remind_title+"的日子，请记得哦！"))/*.setContentIntent(contentIntent)*//*.setAutoCancel(true)*/
                            .setContentIntent(PendingIntent.getService(this, 0, new Intent(this,CancelNoticeService.class)
                                    .putExtra("notifyCode",dayId), 0)).build();

                    startForeground(dayId,notification);
                    /*notificationManager.notify(i++, notification);*/
                    Toast.makeText(getApplicationContext(),"------明天-----",Toast.LENGTH_LONG).show();
                    if("按年".equals(ifrepeat)){
                        ContentValues cv=new ContentValues();
                        remind_day=CalDayUtil.getNextYear(remind_day);
                        cv.put("remind_day",remind_day);
                        database.update("ItemDetails",cv,"id=?",new String[]{dayId+""});
                    }
                }else if (selected_day.equals(GetSystemDate())){
                    notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    Notification.Builder builder = new Notification.Builder(getApplicationContext())/*.setContentText("今天是" + remind_title + "的日子，好好庆祝下吧！")*/.setContentTitle("纪念日提醒")
                            .setSmallIcon(R.drawable.monkey2).setWhen(System.currentTimeMillis()).setStyle(new Notification.BigTextStyle()
                                    .bigText("今天是" + remind_title + "的日子，耶耶耶！")) .setContentIntent(PendingIntent.getService(this,
                                    0, new Intent(this,CancelNoticeService.class).putExtra("notifyCode",dayId), 0));
        /*            Intent appIntent = null;
                    appIntent = new Intent(getApplicationContext(), MainActivity.class);
                    appIntent.setAction(Intent.ACTION_MAIN);
                    appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    appIntent.putExtra("flag", "notifyIntent");
                    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/
                  /*  builder.setContentIntent(contentIntent);*/
                    notification=builder.build();
                    startForeground(dayId,notification);
                    /*notificationManager.notify(i++, notification);*/
                    Toast.makeText(getApplicationContext(),"------今天-----",Toast.LENGTH_LONG).show();
                    if("按年".equals(ifrepeat)){
                        ContentValues cv=new ContentValues();
                        remind_day=CalDayUtil.getNextYear(remind_day);
                        cv.put("remind_day",remind_day);
                        database.update("ItemDetails",cv,"id=?",new String[]{dayId+""});
                    }else if ("按月".equals(ifrepeat)){
                        ContentValues cv=new ContentValues();
                        remind_day=CalDayUtil.getNextMonth(remind_day);
                        cv.put("remind_day",remind_day);
                        database.update("ItemDetails",cv,"id=?",new String[]{dayId+""});

                    }
                }
            }
            database.close();
        }
    }
    private void shake() {
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        //数组的a[0]表示静止的时间，a[1]代表的是震动的时间，
        long[] time = {0, 500};
        //震动的毫秒值
        //vibrate的第二参数表示从哪里开始循环
        vibrator.vibrate(time, -1);
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.remind);
        mMediaPlayer.start();
    }

    private String getTomorrowDate(String selectDay){

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
    private void dayRemind(int hour,Intent intent) {

        Intent intent3 = new Intent(getApplicationContext(),DayReceiver.class);
        intent3.setAction("DayRemind");
        PendingIntent pi = PendingIntent.getBroadcast(DayRemindService.this, 5 , intent3, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); //  这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY,hour);//设置为8：00点提醒
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long selectTime = calendar.getTimeInMillis();    //计算出设定的时间
        //  如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        long systemTime = System.currentTimeMillis();
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        if (am.getNextAlarmClock()!=null) {

            Log.i("DutyService", String.valueOf(am.getNextAlarmClock().getTriggerTime()));
        }else {
            Log.i("DutyService", "alarm null");
        }
        am.setRepeating(AlarmManager.RTC_WAKEUP, selectTime,am.INTERVAL_DAY,pi);
        Toast.makeText(getApplicationContext(),"DayServiceRuning",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dayRemind(SpUtils.getInt(getApplicationContext(), ConstantValue.SELECT_REMIND_TIME,8),new Intent());
    }
}
