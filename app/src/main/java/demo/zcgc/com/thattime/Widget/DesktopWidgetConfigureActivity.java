package demo.zcgc.com.thattime.Widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.time.Instant;
import java.util.Arrays;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.activity.AddItemActivity;
import demo.zcgc.com.thattime.activity.MainActivity;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;

/**
 * The configuration screen for the {@link DesktopWidget DesktopWidget} AppWidget.
 */
public class DesktopWidgetConfigureActivity extends Activity {

    private String[] db_data = {}, db_data1 = {}, db_data_2 = {}, db_data2 = {}, db_data_3 = {}, db_data3 = {}, db_data_4 = {}, db_data4 = {},db_data_5,db_data5;
    private static final String PREFS_NAME = "demo.zcgc.com.thattime.Widget.DesktopWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    /*EditText mAppWidgetText;*/
    GridView gv_choose_card;
  /*  View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = DesktopWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            *//*String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            DesktopWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);*//*

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };*/
    private int db_id;
    private String db_create_time,db_title_des,db_day_distance,db_selected_day,db_background,if_imp;
    private int db_data_length1,db_data_length,db_data_length_2,db_data_length2,db_data_length_3,db_data_length3,db_data_length_4,db_data_length4,db_data_length_5,db_data_length5;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase database;
    private MyAdapter myAdapter;
    private TextView tv_widget_title,tv_widget_remining_day,tv_widget_select_day;
    private TextView tv_create_time;
    private String tag="DWCA";
    private RelativeLayout cl_widget_view;
    private Bitmap bitmap;

    public DesktopWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
    private void initDataBase() {
        db_data = new String[]{};
        db_data_2 = new String[]{};
        db_data_3 = new String[]{};
        db_data_4 = new String[]{};
        db_data_5 = new String[]{};
        if (db_data.length == 0) {
            Log.i("DWCA", "db_data被重置");
        }
        Cursor cursor = database.query("ItemDetails", null, null, null, null, null, null, null);
        //遍历Curosr对象，取出数据并打印
        while (cursor.moveToNext()) {
            db_id = cursor.getInt(cursor.getColumnIndex("id"));

            db_create_time = cursor.getString(cursor.getColumnIndex("create_time"));

            db_title_des = cursor.getString(cursor.getColumnIndex("title_des"));

            db_day_distance = cursor.getString(cursor.getColumnIndex("day_distance"));

            db_selected_day = cursor.getString(cursor.getColumnIndex("selected_day"));

            db_background = cursor.getString(cursor.getColumnIndex("background"));

            if_imp = cursor.getString(cursor.getColumnIndex("if_imp"));

            Log.d("DWAT", "id:" + db_id + "title_des:" + db_title_des + " day_distance:" + db_day_distance + " selected_day:" + db_selected_day + " background:" + db_background);

            /*db_data1*/
            db_data1 = new String[]{db_title_des};
            db_data_length1 = db_data.length;
            db_data_length = db_data.length + db_data1.length;
            db_data = Arrays.copyOf(db_data, db_data_length);
            System.arraycopy(db_data1, 0, db_data, db_data_length1, db_data1.length);
            /*db_data2*/
            db_data2 = new String[]{db_create_time};
            db_data_length_2 = db_data_2.length;
            db_data_length2 = db_data_2.length + db_data2.length;
            db_data_2 = Arrays.copyOf(db_data_2, db_data_length2);
            System.arraycopy(db_data2, 0, db_data_2, db_data_length_2, db_data2.length);
            /*db_data3*/
            db_data3 = new String[]{db_selected_day};
            db_data_length_3 = db_data_3.length;
            db_data_length3 = db_data_3.length + db_data3.length;
            db_data_3 = Arrays.copyOf(db_data_3, db_data_length3);
            System.arraycopy(db_data3, 0, db_data_3, db_data_length_3, db_data3.length);
            /*   db_data4*/
            db_data4 = new String[]{db_background};
            db_data_length_4 = db_data_4.length;
            db_data_length4 = db_data_4.length + db_data4.length;
            db_data_4 = Arrays.copyOf(db_data_4, db_data_length4);
            System.arraycopy(db_data4, 0, db_data_4, db_data_length_4, db_data4.length);

            db_data5 = new String[]{if_imp};
            db_data_length_5 = db_data_5.length;
            db_data_length5 = db_data_5.length + db_data5.length;
            db_data_5 = Arrays.copyOf(db_data_5, db_data_length5);
            System.arraycopy(db_data5, 0, db_data_5, db_data_length_5, db_data5.length);
        }
        //关闭Cursor
        cursor.close();

        myAdapter.notifyDataSetChanged();
    }
    private int ItemHeight() {
        int itemheight = 0;
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        itemheight = (int) (height * 0.38);
        return itemheight;
    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return db_data.length;
        }

        @Override
        public Object getItem(int i) {
            String[] data_str = new String[]{db_data[i], CalDayUtil.distance_day(db_data_3[i]), db_data_3[i]};
            return data_str;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View view1 = View.inflate(getApplicationContext(), R.layout.widget_layout, null);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ItemHeight());
            view1.setLayoutParams(param);
            tv_widget_title = view1.findViewById(R.id.tv_widget_title);
            tv_widget_remining_day = view1.findViewById(R.id.tv_widget_reminday_day);
            tv_widget_select_day = view1.findViewById(R.id.tv_widget_select_day);
            cl_widget_view=view1.findViewById(R.id.cl_widget_view);


            tv_widget_title.setText(db_data[i]);
            Log.i(tag, "设置了" + tv_widget_title.getText().toString());


            tv_widget_remining_day.setText(CalDayUtil.distance_day(db_data_3[i]));
            Log.i(tag, "设置了2" + tv_widget_remining_day.getText().toString());


            tv_widget_select_day.setText(db_data_3[i]);
            Log.i(tag, "设置了3" + tv_widget_select_day.getText().toString());

            if (db_data_4[i].substring(0, 1).equals("#")) {
                cl_widget_view.setBackgroundColor(Color.parseColor(db_data_4[i]));
            } else {
                bitmap = BitmapFactory.decodeFile(db_data_4[i]);
                cl_widget_view.setBackground(new BitmapDrawable(bitmap));
            }
            Log.i(tag, "设置了4" + cl_widget_view.getBackground());

            return view1;
        }
    }
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.desktop_widget_configure);
        db_data = new String[]{};
        db_data_2 = new String[]{};
        db_data_3 = new String[]{};
        db_data_4 = new String[]{};
        db_data_5 = new String[]{};

        myDatabaseHelper = new MyDatabaseHelper(getApplicationContext(), "ItemDetails.db", null, 1);
        database = myDatabaseHelper.getWritableDatabase();

        gv_choose_card=findViewById(R.id.gv_choose_card);
        myAdapter=new MyAdapter();
        gv_choose_card.setAdapter(myAdapter);
        initDataBase();
        /*mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);*/
         //Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        gv_choose_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Context context = DesktopWidgetConfigureActivity.this;

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                DesktopWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                Log.i("DWCA-------------->","TETLE_DES="+db_data[position]);
                SpUtils.putString(context, ConstantValue.DESKTOP_TITLE_DES,db_data[position]);
                SpUtils.putString(context,ConstantValue.DESKTOP_CREATE_TIME,db_data_2[position]);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
      /*  mAppWidgetText.setText(loadTitlePref(DesktopWidgetConfigureActivity.this, mAppWidgetId));*/
    }
}

