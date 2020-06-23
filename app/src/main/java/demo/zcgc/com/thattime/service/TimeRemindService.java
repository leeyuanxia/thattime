package demo.zcgc.com.thattime.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import demo.zcgc.com.thattime.BroadcastReceiver.AlarmReceiver;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;


public class TimeRemindService extends Service {
    private AlarmManager am,am2;
    private int flag;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase database;
    private String remindDay="";
    private String remind_title;
    private Intent intent2;
    private Intent intent3;
    private static int healthy_code,day_code;

    //  private ArrayList<HashMap<String,String>> alResult;
 //   private HashMap<String,String> hm_values;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
  //  public static String path = "data/data/demo.zcgc.com.thattime/databases/ItemDetails.db";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        am=(AlarmManager)getSystemService(ALARM_SERVICE);

            initRemind(8, 30, AlarmReceiver.class, 1);
            initRemind(13, 30, AlarmReceiver.class, 2);
            initRemind(22, 30, AlarmReceiver.class, 3);
            Toast.makeText(getApplicationContext(),"Healthy开启",Toast.LENGTH_SHORT).show();

      flags=START_STICKY;
      return super.onStartCommand(intent,flags, startId);
    }

    private void initRemind(int huorOfDay, int minute, Class CT,int requestCode) {

        intent2 = new Intent(this,CT );
        intent2.putExtra("requestcode",requestCode);
        intent2.setAction("HealthyRemind");
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), requestCode , intent2, 0);
        long systemTime = System.currentTimeMillis();//java.lang.System.currentTimeMillis()，它返回从 UTC 1970 年 1 月 1 日午夜开始经过的毫秒数。
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); //  这里时区需要设置一下，不然会有8个小时的时间差
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, huorOfDay);//设置为8：00点提醒
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);            //选择的定时时间
        long selectTime = calendar.getTimeInMillis();    //计算出设定的时间
        //  如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        // 进行闹铃注册

        am.setRepeating(AlarmManager.RTC_WAKEUP, selectTime,am.INTERVAL_DAY,pi);
    }

}


