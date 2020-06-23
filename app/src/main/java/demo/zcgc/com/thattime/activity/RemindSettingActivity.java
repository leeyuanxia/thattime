package demo.zcgc.com.thattime.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import demo.zcgc.com.thattime.BroadcastReceiver.DayReceiver;
import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.engine.DrawableSwitch;
import demo.zcgc.com.thattime.service.DayRemindService;
import demo.zcgc.com.thattime.service.TimeRemindService;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.ServiceUtil;
import demo.zcgc.com.thattime.uitls.SpUtils;

public class RemindSettingActivity extends AppCompatActivity {

    private DrawableSwitch ds_healthy,ds_day;
    private ImageView iv_remind_setting_back;
    private boolean isrunning;
    private int healthy_code,day_code;
    private View cl_time_select;
    private static String[] data = { "0点", "1点", "2点", "3点",
            "4点", "5点", "6点", "7点", "8点", "9点" , "10点",
            "11点", "12点", "13点", "14点", "15点", "16点", "17点", "18点",
            "19点", "20点", "21点", "22点", "23点"};
    private ConstraintLayout cl_list_view;
    private ImageView iv_select_more;
    private TextView tv_selected_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_setting);

        initUI();
        iinitData();
        initTime();
        /*initCode();*/
    }

    private void iinitData() {

        //显示是否开启
        isrunning = ServiceUtil.isRunning(RemindSettingActivity.this, "demo.zcgc.com.thattime.service.TimeRemindService");
        iv_remind_setting_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemindSettingActivity.this, MoreActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        ds_healthy.setListener(new DrawableSwitch.MySwitchStateChangeListener() {
            @Override
            public void mySwitchStateChanged(boolean isSwitchOn) {

                if (isSwitchOn){
                    startService(new Intent(getApplicationContext(), TimeRemindService.class));
                    SpUtils.putBoolean(getApplicationContext(), ConstantValue.HEALTHY_REMIND_IF_OPEN,true);

                }else {

                    stopService(new Intent(getApplicationContext(), TimeRemindService.class));
                    SpUtils.putBoolean(getApplicationContext(), ConstantValue.HEALTHY_REMIND_IF_OPEN,false);

                }
            }
        });
        ds_healthy.setSwitchOn(ServiceUtil.isRunning(RemindSettingActivity.this, "demo.zcgc.com.thattime.service.TimeRemindService"));
        ds_day.setListener(new DrawableSwitch.MySwitchStateChangeListener() {
            @Override
            public void mySwitchStateChanged(boolean isSwitchOn) {

                if (isSwitchOn){

                        SpUtils.putBoolean(getApplicationContext(), ConstantValue.DAY_REMIND_IF_OPEN,true);
                        startService(new Intent(getApplicationContext(), DayRemindService.class));

                }else {

                    stopService(new Intent(getApplicationContext(), DayRemindService.class));
                    SpUtils.putBoolean(getApplicationContext(), ConstantValue.DAY_REMIND_IF_OPEN,false);

                }
            }
        });
        ds_day.setSwitchOn(ServiceUtil.isRunning(RemindSettingActivity.this, "demo.zcgc.com.thattime.service.DayRemindService"));

        cl_time_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ExchangeView();
            }
        });

        tv_selected_time.setText(SpUtils.getInt(getApplicationContext(),ConstantValue.SELECT_REMIND_TIME,8)+"点");
    }
    private void ExchangeView() {
        if (cl_list_view.getVisibility()==View.GONE){
            cl_list_view.setVisibility(View.VISIBLE);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            iv_select_more.setBackgroundResource(R.drawable.ic_expand_less_black_24dp);
        }else if (cl_list_view.getVisibility()==View.VISIBLE){
            cl_list_view.setVisibility(View.GONE);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            iv_select_more.setBackgroundResource(R.drawable.ic_expand_more_black_24dp);
        }
    }

    private void initUI() {
        ds_healthy=findViewById(R.id.drawableSwitch);
        ds_day=findViewById(R.id.drawableSwitch2);

        iv_remind_setting_back=findViewById(R.id.iv_Remind_setting_back);
        cl_time_select=findViewById(R.id.cl_time_select);
        cl_list_view=findViewById(R.id.cl_list_view);

        iv_select_more=findViewById(R.id.iv_select_more);
        tv_selected_time=findViewById(R.id.tv_selected_time);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RemindSettingActivity.this, MoreActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    private void initTime() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,R.layout.my_list_view,data);

        ListView listView=findViewById(R.id.lv_time_down);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpUtils.putInt(getApplicationContext(), ConstantValue.SELECT_REMIND_TIME,position);
                tv_selected_time.setText(SpUtils.getInt(getApplicationContext(),ConstantValue.SELECT_REMIND_TIME,8)+"点");
                ExchangeView();

                if (ServiceUtil.isRunning(RemindSettingActivity.this, "demo.zcgc.com.thattime.service.DayRemindService"))
                { startService(new Intent(getApplicationContext(),DayRemindService.class)); }

            }
        });
    }
}
