package demo.zcgc.com.thattime.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.EventLog;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;

public class RepeatActivity extends Activity {
    private ConstraintLayout cl_back;
    private ConstraintLayout cl_all,cl_repeat_null,cl_repeat_year,cl_repeat_month;
    private CheckBox cb_repeat_null,cb_repeat_year,cb_repeat_month;
    private int mId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat);


        initUI();
        initnotification();
        initData();
    }

    private void initData() {
        mId=SpUtils.getInt(getApplicationContext(),ConstantValue.REPEAT_ISCHEAKED,R.id.cb_repeat_null);
        switch (mId){
            case R.id.cb_repeat_null:
                cb_repeat_null.setChecked(true);
                break;
            case R.id.cb_repeat_year:
                cb_repeat_year.setChecked(true);
                break;
            case R.id.cb_repeat_month:
                cb_repeat_month.setChecked(true);
                break;
        }

    }

    private void initnotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initUI() {

        cl_back = findViewById(R.id.cl_back);
        cl_all = findViewById(R.id.cl_all);

        cb_repeat_null=findViewById(R.id.cb_repeat_null);
        cb_repeat_year=findViewById(R.id.cb_repeat_year);
        cb_repeat_month=findViewById(R.id.cb_repeat_month);

        cl_repeat_null=findViewById(R.id.cl_repeat_null);
        cl_repeat_year=findViewById(R.id.cl_repeat_year);
        cl_repeat_month=findViewById(R.id.cl_repeat_month);


        cl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);

            }
        });

        cl_repeat_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                intent.putExtra("ifRepeat","不重复");
                setResult(1,intent);
                SpUtils.putInt(getApplicationContext(),ConstantValue.REPEAT_ISCHEAKED,R.id.cb_repeat_null);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
            }
        });

        cl_repeat_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("ifRepeat","按年");
                setResult(2,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(),ConstantValue.REPEAT_ISCHEAKED,R.id.cb_repeat_year);
            }
        });

        cl_repeat_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent=new Intent(RepeatActivity.this,AddItemActivity.class);
                startActivity(intent);*/
              /*cb_repeat_month.setChecked(true);*/
                Intent intent=new Intent();
                intent.putExtra("ifRepeat","按月");
                setResult(3,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(),ConstantValue.REPEAT_ISCHEAKED,R.id.cb_repeat_month);
            }
        });
      /*  SpUtils.putInt(getApplicationContext(), ConstantValue.REPEAT_ISCHEAKED,isRepeat());*/

    }
}
