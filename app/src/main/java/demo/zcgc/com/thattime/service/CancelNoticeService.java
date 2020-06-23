package demo.zcgc.com.thattime.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


import demo.zcgc.com.thattime.R;

/** 移除前台Service通知栏标志，这个Service选择性使用
 *
 * Created by jianddongguo on 2017/7/7.
 */
public class CancelNoticeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("cancleRunning","Servicerun");
         int notifyCode=intent.getIntExtra("notifyCode",0);

            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.drawable.monkey);
            startForeground(notifyCode,builder.build());
            Log.i("cancelNotice", "onStartCommand: "+notifyCode);

                    // 取消CancelNoticeService的前台
                    stopForeground(true);
                    // 移除DaemonService弹出的通知
                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(notifyCode);
                    // 任务完成，终止自己
                    stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}