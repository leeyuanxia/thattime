package demo.zcgc.com.thattime.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.ConstraintTableLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;

public class RemindActivity extends Activity {
    private ConstraintLayout cl_back;
    private ConstraintLayout cl_all;
    private int mId;
    private CheckBox cb_remind_null,cb_remind_today,cb_remind_preday;
    private ConstraintLayout cl_remind_null,cl_remind_today,cl_remind_preday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);


        initUI();
        initnotification();
        initData();
    }

    private void initData() {
        mId= SpUtils.getInt(getApplicationContext(), ConstantValue.REMIND_ISCHEAKED,R.id.cb_remind_null);
        switch (mId){
            case R.id.cb_remind_null:
                cb_remind_null.setChecked(true);
                break;
            case R.id.cb_remind_today:
                cb_remind_today.setChecked(true);
                break;
            case R.id.cb_remind_preday:
                cb_remind_preday.setChecked(true);
                break;
        }
    }

    private void initnotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        }

    }

    private void initUI() {


        cl_back = findViewById(R.id.cl_back);
        cl_all = findViewById(R.id.cl_all);
        cl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(RemindActivity.this,AddItemActivity.class);
                startActivity(intent);*/
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);

            }
        });
        cb_remind_null=findViewById(R.id.cb_remind_null);
        cb_remind_today=findViewById(R.id.cb_remind_today);
        cb_remind_preday=findViewById(R.id.cb_remind_preday);

        cl_remind_null=findViewById(R.id.cl_remind_null);
        cl_remind_today=findViewById(R.id.cl_remind_today);
        cl_remind_preday=findViewById(R.id.cl_remind_preday);


        cl_remind_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(RepeatActivity.this,AddItemActivity.class);
                startActivity(intent);*/
                Intent intent=new Intent();
                intent.putExtra("ifRemind","不提醒");
                setResult(4,intent);
                SpUtils.putInt(getApplicationContext(),ConstantValue.REMIND_ISCHEAKED,R.id.cb_remind_null);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);

            }
        });

        cl_remind_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(RepeatActivity.this,AddItemActivity.class);
                startActivity(intent);*/
                Intent intent=new Intent();
                intent.putExtra("ifRemind","当天提醒");
                setResult(5,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(),ConstantValue.REMIND_ISCHEAKED,R.id.cb_remind_today);
            }
        });

        cl_remind_preday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent=new Intent(RepeatActivity.this,AddItemActivity.class);
                startActivity(intent);*/
                Intent intent=new Intent();
                intent.putExtra("ifRemind","提前一天提醒");
                setResult(6,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(),ConstantValue.REMIND_ISCHEAKED,R.id.cb_remind_preday);
            }
        });


        }

}
