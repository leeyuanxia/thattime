package demo.zcgc.com.thattime.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.ResponseCache;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.FileUtils;

public class SuggestActivity extends AppCompatActivity {

    private ImageView iv_suggest_back;
    private EditText et_suggest_context,et_suggest_title;
    private Button bt_suggest_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);

        initUI();

        initData();
    }

    private void initData() {
        iv_suggest_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuggestActivity.this,AboutActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        bt_suggest_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_suggest_title.getText().toString().equals("")&&!et_suggest_context.getText().toString().equals("")){
                    final String fileName=et_suggest_title.getText().toString()+"+"+android.os.Build.MODEL+"+"+CalDayUtil.getCreateTime()+".txt";
                new FileUtils().generateTxt(getApplicationContext(),fileName,"标题："+et_suggest_title.getText().toString()+"       " +
                        "           "+"内容："+et_suggest_context.getText().toString());
                    et_suggest_context.setText("");
                    et_suggest_title.setText("");
                    Toast.makeText(getApplicationContext(),"反馈成功",Toast.LENGTH_SHORT).show();
                   new Thread(){
                       @Override
                       public void run() {
                           //10.24.160.140
                           try {
                               String response= FileUtils.sendFile("http://39.106.20.0:8080/day11_4/ReciveServlet",
                                       "/sdcard/Suggestes/"+fileName, fileName); /*FileUtils.sendFile("http://192.168.1.1:8080/day11_4/ReciveServlet",
                                       "/sdcard/Suggestes/"+fileName, fileName);*/
                               Log.i("文件上传","成功"+response);


                           }catch (Exception e){
                               Log.i("文件上传","错误:"+e.toString());
                           }
                   }
                }.start();
                }
                else {
                    Toast.makeText(getApplicationContext(),"标题或内容不能为空，请填写",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUI() {
        iv_suggest_back=findViewById(R.id.iv_suggest_back);

        et_suggest_title=findViewById(R.id.et_suggest_title);
        et_suggest_context=findViewById(R.id.et_suggest_context);

        bt_suggest_submit=findViewById(R.id.bt_suggest_submit);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SuggestActivity.this,AboutActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
