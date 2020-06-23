package demo.zcgc.com.thattime.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;

public class ColorActivity extends Activity{
    private ConstraintLayout cl_back;
    private ConstraintLayout cl_color_yelow,cl_color_green,cl_color_blue,cl_color_orange,cl_color_red;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        initUI();
        initnotification();
    }
    private void initnotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /*getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        }

    }
    private void initUI() {
        cl_back=findViewById(R.id.cl_color_back);
        cl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
            }
        });

        cl_color_yelow=findViewById(R.id.cl_color_yellow);
        cl_color_yelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                setResult(7,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(), ConstantValue.BACKGROUND_COLOR, Color.parseColor("#F5F5DC"));
            }
        });

        cl_color_green=findViewById(R.id.cl_color_green);
        cl_color_green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                setResult(8,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(), ConstantValue.BACKGROUND_COLOR,Color.parseColor("#00ff00"));
            }
        });

        cl_color_blue=findViewById(R.id.cl_color_blue);
        cl_color_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                setResult(9,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(), ConstantValue.BACKGROUND_COLOR,Color.parseColor("#00BFFF"));
            }
        });

        cl_color_orange=findViewById(R.id.cl_color_orange);
        cl_color_orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                setResult(10,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(), ConstantValue.BACKGROUND_COLOR,Color.parseColor("#FFA500"));
            }
        });

        cl_color_red=findViewById(R.id.cl_color_red);
        cl_color_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                setResult(11,intent);
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
                SpUtils.putInt(getApplicationContext(), ConstantValue.BACKGROUND_COLOR,Color.parseColor("#FF0000"));
            }
        });

    }
}
