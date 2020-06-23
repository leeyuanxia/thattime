package demo.zcgc.com.thattime.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;

public class SelectTimeActivity extends Activity {
    private String[] data = { "0点", "1点", "2点", "3点",
            "4点", "5点", "6点", "7点", "8点", "9点" , "10点",
            "11点", "12点", "13点", "14点", "15点", "16点", "17点", "18点",
            "19点", "20点", "21点", "22点", "23点"};
    private ConstraintLayout cl_select_time_back,cl_select_time_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        initUI();
        initTime();
        initnotification();
    }

    private void initUI() {
        cl_select_time_back=findViewById(R.id.cl_select_time_back);
        cl_select_time_all=findViewById(R.id.cl_select_time_all);

        cl_select_time_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);

            }
        });
    }

    private void initnotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private void initTime() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
        this,android.R.layout.simple_list_item_1,data);
        ListView listView=findViewById(R.id.lv_time_down);
        listView.setAdapter(adapter);
        listView.setBackgroundColor(Color.parseColor("#000000"));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpUtils.putString(getApplicationContext(), ConstantValue.SELECT_REMIND_TIME,position+"点");
                finish();
                overridePendingTransition(0,R.anim.slide_down_out);
            }
        });
    }
}
