package demo.zcgc.com.thattime.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.zip.Inflater;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.views.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_more;
    //定义变量判断是否退出
    private static boolean isExit = false;
    private ImageView iv_add;
    private GridView gv_item_details;
    private TextView tv_rougly_title, tv_remining_day, tv_selected_day;
    private String title_des, day_distance, selected_day;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day;
    private String dataString;
    private String tag = "MainActivity";
    private MyAdapter myAdapter;
    private ImageView iv_edit, iv_star_set, iv_delete, iv_star;
    private ConstraintLayout cl_grid_view_1, cl_grid_view_2;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase database;
    private ContentValues contentvalues = new ContentValues();
    private int id, Returnid, db_data_length_5, db_data_length5, db_data_length1, db_data_length, db_data_length2, db_data_length_2, db_data_length3, db_data_length_3, db_data_length_4, db_data_length4;
    private String db_day_distance = "", db_selected_day = "", db_background = "", db_title_des = "";
    private String[] db_data = {}, db_data1 = {}, db_data_2 = {}, db_data2 = {}, db_data_3 = {}, db_data3 = {}, db_data_4 = {}, db_data4 = {};
    private String Return_title_des, Return_create_time,Test="";
    private TextView tv_create_time;
    private String db_create_time = "";
    private Bitmap bitmap;
    private View view_cl_grid_view_1, view_cl_grid_view_2, view_iv_star;
    private String if_imp;
    private String[] db_data5;
    private String[] db_data_5;

    private Dialog dialog;
    private View inflate;
    private TextView tv_delete_no, tv_delete_yes;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initUI();
        initGridView();
        db_data = new String[]{};
        db_data_2 = new String[]{};
        db_data_3 = new String[]{};
        db_data_4 = new String[]{};
        db_data_5 = new String[]{};
        inititemdate();
        initDataBase();

    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

    private void initDataBase() {
        db_data = new String[]{};
        db_data_2 = new String[]{};
        db_data_3 = new String[]{};
        db_data_4 = new String[]{};
        db_data_5 = new String[]{};
        if (db_data.length == 0) {
            Log.i(tag, "db_data被重置");
        }
        Cursor cursor = database.query("ItemDetails", null, null, null, null, null, null, null);
        //遍历Curosr对象，取出数据并打印
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("id"));

            db_create_time = cursor.getString(cursor.getColumnIndex("create_time"));

            db_title_des = cursor.getString(cursor.getColumnIndex("title_des"));

            db_day_distance = cursor.getString(cursor.getColumnIndex("day_distance"));

            db_selected_day = cursor.getString(cursor.getColumnIndex("selected_day"));

            db_background = cursor.getString(cursor.getColumnIndex("background"));

            if_imp = cursor.getString(cursor.getColumnIndex("if_imp"));

            Log.d("MainActivity", "id:" + id + "title_des:" + db_title_des + " day_distance:" + db_day_distance + " selected_day:" + db_selected_day + " background:" + db_background);

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


    private void inititemdate() {
        dataString = CalDayUtil.GetSystemDate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 103:
                if (resultCode == 104) {

                    contentvalues.put("title_des", data.getStringExtra("title_des"));
                    contentvalues.put("day_distance", data.getStringExtra("day_distance"));
                    contentvalues.put("selected_day", data.getStringExtra("selected_day"));
                    contentvalues.put("background", data.getStringExtra("background"));
                    contentvalues.put("create_time", data.getStringExtra("create_time"));
                    contentvalues.put("ifremind",data.getStringExtra("ifremind"));
                    contentvalues.put("ifrepeat",data.getStringExtra("ifrepeat"));
                    if (data.getStringExtra("ifremind").equals("不提醒"))
                    {
                        contentvalues.put("remind_day","");
                    }else if (data.getStringExtra("ifremind").equals("当天提醒"))
                    {
                        contentvalues.put("remind_day",CalDayUtil.dataString);
                    }else {
                        contentvalues.put("remind_day",CalDayUtil.getformerDate(data.getStringExtra("selected_day")));
                    }
                    contentvalues.put("if_imp", "no");
                    database.insert("ItemDetails", null, contentvalues);

                    initDataBase();
                }
            case 201:
                if (resultCode == 202) {
                    myAdapter.notifyDataSetChanged();
                }
        }

    }

    private void initGridView() {

        gv_item_details = findViewById(R.id.gv_item_details);
        myAdapter = new MyAdapter();
        gv_item_details.setAdapter(myAdapter);

        gv_item_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cl_grid_view_1 = view.findViewById(R.id.cl_grid_ten_view_1);
                cl_grid_view_2 = view.findViewById(R.id.cl_grid_ten_view_2);
                if (cl_grid_view_1.getVisibility() == View.GONE) {
                    cl_grid_view_1.setVisibility(View.VISIBLE);
                    cl_grid_view_2.setVisibility(View.GONE);
                    Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                    cl_grid_view_1.setAnimation(animation2);
                    cl_grid_view_2.setAnimation(animation);

                }
            }
        });
        gv_item_details.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_rougly_title = view.findViewById(R.id.tv_roughly_title);
                Return_title_des = db_data[i];
                tv_create_time = view.findViewById(R.id.tv_create_time);
                Return_create_time = db_data_2[i];
                view_iv_star = view.findViewById(R.id.iv_star);
                Returnid = ReturnID(i);


                cl_grid_view_1 = view.findViewById(R.id.cl_grid_ten_view_1);

                cl_grid_view_2 = view.findViewById(R.id.cl_grid_ten_view_2);

                view_cl_grid_view_1 = view.findViewById(R.id.cl_grid_ten_view_1);

                view_cl_grid_view_2 = view.findViewById(R.id.cl_grid_ten_view_2);
                ExChangeView();
                return true;
            }
        });


    }

    private void ExChangeView() {
        if (cl_grid_view_1.getVisibility() == View.VISIBLE && cl_grid_view_2.getVisibility() == View.GONE) {
            cl_grid_view_1.setVisibility(View.GONE);
            cl_grid_view_2.setVisibility(View.VISIBLE);
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            cl_grid_view_1.setAnimation(animation2);
            cl_grid_view_2.setAnimation(animation);
            Log.i(tag, "进行了交换");

        } else if (cl_grid_view_2.getVisibility() == View.VISIBLE && cl_grid_view_1.getVisibility() == View.GONE) {
            cl_grid_view_1.setVisibility(View.VISIBLE);
            cl_grid_view_2.setVisibility(View.GONE);
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            cl_grid_view_1.setAnimation(animation2);
            cl_grid_view_2.setAnimation(animation);
            Log.i(tag, "进行了交换");
        }

    }

    private int ReturnID(int i) {

        return i + 1;
    }


    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();

        } else {
            //单机一次提示信息
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            isExit = true;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    isExit = false;
                }
            }.start();
        }
    }


    private void initUI() {
        myDatabaseHelper = new MyDatabaseHelper(getApplicationContext(), "ItemDetails.db", null, 1);
        database = myDatabaseHelper.getWritableDatabase();
        iv_more = findViewById(R.id.iv_more);

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                startActivityForResult(intent, 201);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        iv_add = findViewById(R.id.add_item);

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra("request_code","no");
                startActivityForResult(intent, 103);
             //   finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


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
            View view1 = View.inflate(getApplicationContext(), R.layout.grid_item_view, null);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ItemHeight());
            view1.setLayoutParams(param);
            tv_rougly_title = view1.findViewById(R.id.tv_roughly_title);
            tv_rougly_title = view1.findViewById(R.id.tv_roughly_title);
            tv_remining_day = view1.findViewById(R.id.tv_remaining_day);
            tv_selected_day = view1.findViewById(R.id.tv_selected_day);
            iv_edit = view1.findViewById(R.id.iv_edit);
            iv_star_set = view1.findViewById(R.id.iv_to_top);
            iv_delete = view1.findViewById(R.id.iv_delete);
            tv_create_time = view1.findViewById(R.id.tv_create_time);
            cl_grid_view_1 = view1.findViewById(R.id.cl_grid_ten_view_1);
            cl_grid_view_2 = view1.findViewById(R.id.cl_grid_ten_view_2);
            iv_star = view1.findViewById(R.id.iv_star);


            if (db_data_5[i].equals("yes")) {
                if (db_data_4[i].equals("#FF0000")) {
                    iv_star.setBackgroundResource(R.drawable.star_imp_blue);

                }

                iv_star.setVisibility(View.VISIBLE);
            } else {
                iv_star.setVisibility(View.GONE);
            }

            tv_rougly_title.setText(db_data[i]);
            Log.i(tag, "设置了" + tv_rougly_title.getText().toString());


            tv_remining_day.setText(CalDayUtil.distance_day(db_data_3[i]));
            Log.i(tag, "设置了2" + tv_remining_day.getText().toString());


            tv_selected_day.setText(db_data_3[i]);
            Log.i(tag, "设置了3" + tv_selected_day.getText().toString());

            if (db_data_4[i].substring(0, 1).equals("#")) {
                cl_grid_view_1.setBackgroundColor(Color.parseColor(db_data_4[i]));
            } else {
                bitmap = BitmapFactory.decodeFile(db_data_4[i]);
                cl_grid_view_1.setBackground(new BitmapDrawable(bitmap));
            }
            Log.i(tag, "设置了4" + cl_grid_view_1.getBackground());

            tv_create_time.setText(db_data_2[i]);

            iv_delete.setOnClickListener(new DeleteClick());

            iv_star_set.setOnClickListener(new ToTopClick());

            iv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,AddItemActivity.class);
                    intent.putExtra("request_code","edit_item");
                    intent.putExtra("title_des",db_data[i]);
                    intent.putExtra("selected_day",db_data_3[i]);
                    intent.putExtra("background",db_data_4[i]);
                    intent.putExtra("create_time",db_data_2[i]);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
            });

            return view1;
        }
    }


    private int ItemHeight() {
        int itemheight = 0;
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        itemheight = (int) (height * 0.38);
        return itemheight;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDataBase();
    }

    private class DeleteClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            show();
        }
    }


    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.delete_dialog_layout, null);        //初始化控件
        tv_delete_yes = inflate.findViewById(R.id.tv_delete_yes);
        tv_delete_no = inflate.findViewById(R.id.tv_delete_no);
        tv_delete_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        /*        if (!db_data_4[Returnid-1].substring(0, 1).equals("#")) {
                    File picture_path=new File(db_data_4[Returnid-1]);
                    picture_path.delete();
                }*/
                Log.i(tag, "返回的create_time是------->" + Return_create_time + "返回的id是------->" + Returnid + "返回的title_des是-------->" + Return_title_des);
                database.delete("ItemDetails", "title_des =? AND create_time =?", new String[]{Return_title_des, Return_create_time});
                initDataBase();
                dialog.dismiss();
            }
        });
        tv_delete_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });        //将布局设置给Dialog
        dialog.setContentView(inflate);        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);        //获得窗体的属性
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(params);
        dialog.show();//显示对话框
    }


    private class ToTopClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.i(tag, "返回的create_time是------->" + Return_create_time + "返回的id是------->" + Returnid);
            if (db_data_5[Returnid - 1].equals("yes")) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("if_imp", "no");
                database.update("ItemDetails", contentValues2, "title_des =? AND create_time =?", new String[]{Return_title_des, Return_create_time});
            } else if (db_data_5[Returnid - 1].equals("no")) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put("if_imp", "yes");
                database.update("ItemDetails", contentValues2, "title_des =? AND create_time =?", new String[]{Return_title_des, Return_create_time});
            }

            if (db_data_4[Returnid - 1].substring(0, 1).equals("#")) {
                Drawable drawableColor = view_cl_grid_view_1.getBackground();
                ColorDrawable colorDrawable = (ColorDrawable) drawableColor;
                if (colorDrawable.getColor() == Color.parseColor("#FF0000")) {
                    view_iv_star.setBackgroundResource(R.drawable.star_imp_blue);
                }
            }

            if (view_iv_star.getVisibility() == View.VISIBLE) {
                view_iv_star.setVisibility(View.GONE);
                view_cl_grid_view_1.setVisibility(View.VISIBLE);
                view_cl_grid_view_2.setVisibility(View.GONE);
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                view_cl_grid_view_1.setAnimation(animation2);
                view_cl_grid_view_2.setAnimation(animation);
                if (view_iv_star.getVisibility() == View.GONE) {
                    Log.i(tag, "设置了不可见");
                }
            } else if (view_iv_star.getVisibility() == View.GONE) {
                view_iv_star.setVisibility(View.VISIBLE);
                view_cl_grid_view_1.setVisibility(View.VISIBLE);
                view_cl_grid_view_2.setVisibility(View.GONE);
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                view_cl_grid_view_1.setAnimation(animation2);
                view_cl_grid_view_2.setAnimation(animation);
                if (view_iv_star.getVisibility() == View.VISIBLE) {
                    Log.i(tag, "设置了可见");
                }
            }
            initDataBase();
        }
    }
}
