package demo.zcgc.com.thattime.activity;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.service.TimeRemindService;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.DataCleanUtil;
import demo.zcgc.com.thattime.uitls.ServiceUtil;
import demo.zcgc.com.thattime.uitls.SpUtils;
import demo.zcgc.com.thattime.views.SettingClickView;
import demo.zcgc.com.thattime.views.SettingItemView;


public class MoreActivity extends AppCompatActivity {
    private SettingClickView clearall;
    private ImageView iv_back;
    private String tag = "MoreActivity";
    private SettingClickView select_remind_time,scv_remind_time;
    private Dialog dialog;
    private View inflate;
    private CheckBox cb_noremind;
    private static SettingItemView setting_Item_View2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);


        initUI();

        initUpdate();
    }

    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.nolonger_remind_dialog, null);        //初始化控件
        cb_noremind = inflate.findViewById(R.id.cb_noremind);
        cb_noremind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.NOLONGER_REMIND, true);
                dialog.dismiss();
            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.TOP);        //获得窗体的属性
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(params);
        dialog.show();//显示对话框
    }

    private void initUpdate() {
        final SettingItemView siv_update = findViewById(R.id.siv_update);
        //获取已有的开关状态，用作显示。
        boolean open_update = SpUtils.getBoolean(MoreActivity.this, ConstantValue.OPEN_UPDATE, false);

        //是否选中，根据上一次的结果
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取之前的选中状态
                Boolean ischeck = siv_update.isCheak();
                siv_update.setCheck(!ischeck);
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !ischeck);


            }
        });
    }

    private void initUI() {
        clearall = findViewById(R.id.scv_1);
        clearall.setTitle("清除全部数据");
        clearall.setDes("点击将提示是否清除全部数据");
        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearDialog();
            }
        });
        //待定
        select_remind_time = findViewById(R.id.scv_2);
        select_remind_time.setTitle("关于该软件");
        select_remind_time.setDes(/*SpUtils.getString(getApplicationContext(),ConstantValue.SELECT_REMIND_TIME,"0点")*/"点击显示细节");
        select_remind_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        scv_remind_time=findViewById(R.id.scv_remind_setting);
        scv_remind_time.setTitle("提醒设置");
        scv_remind_time.setDes("健康提醒和纪念日提醒");
        scv_remind_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MoreActivity.this,RemindSettingActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });


        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MoreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                setResult(202);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                if (isFinishing()) {
                    Log.i(tag, "页面被结束");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MoreActivity.this, MainActivity.class);
        startActivity(intent);
        setResult(202);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    protected void onResume() {
      /*  scv_remind_time.setDes(SpUtils.getString(getApplicationContext(),ConstantValue.SELECT_REMIND_TIME,"8点"));*/
        super.onResume();
    }

    private void showClearDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MoreActivity.this);
        builder.setIcon(R.drawable.ic_announcement_black_24dp);
        builder.setTitle("是否清除全部数据");
        builder.setMessage("点击“是”将清除所有编写的条目，初始化成原始状态！");
        ;
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DataCleanUtil.cleanDatabases(getApplicationContext());
                DataCleanUtil.cleanCustomCache(String.valueOf(new File(Environment.getExternalStorageDirectory(), "takePhotos")));

            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}
