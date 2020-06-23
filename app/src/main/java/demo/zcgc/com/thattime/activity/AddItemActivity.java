package demo.zcgc.com.thattime.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*import com.bumptech.glide.Glide;*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.FileSizeUtil;
import demo.zcgc.com.thattime.uitls.SpUtils;
import demo.zcgc.com.thattime.uitls.ZipPicture;
import demo.zcgc.com.thattime.views.CircleImageView;
import id.zelory.compressor.Compressor;
/*import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;*/
/*import id.zelory.compressor.Compressor;
import io.reactivex.internal.operators.flowable.FlowableFromIterable;
import io.reactivex.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;*/

public class AddItemActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 888;

    private static final int CROP = 200;

    private static boolean isExit = false;


    private ImageView iv_back;
    private TextView mTv_date;
    private EditText mTv_title;
    private ImageView iv_see_total;
    private TextView tv_if_repeat, mTv_anotherView_title, mTv_anotherView_selectedDay, mTv_dayDistance;
    private TextView tv_if_remind;
    private CircleImageView iv_cancel;
    private String tag = "AddItemActivity";
    private ImageView iv_ok;
    private int Code = 0;
    private Calendar calendar = Calendar.getInstance();
    private String date_chosed;
    private ConstraintLayout cl_item, cl_item_all, cl_item_roughly;
    private ImageView iv_background_color, iv_camera, iv_picture;
    private String picturePath;
    private String mTv_title_des;
    private long calDayNumber;
    private int year, day, month, year1 = year, month1, day1, day3, day4;
    private String dataString;
    private String selected_color = "#FFA500";
    private Bitmap bitmap;
    private BitmapDrawable bitmapDrawable;
    private ColorDrawable dra;
    private boolean write_external_storage_allow;
    private Dialog dialog;
    private View inflate;
    private TextView tv_cancel_yes, tv_cancel_no;


    private String request_code = "no";
    private SQLiteDatabase database;
    private String return_title_des, return_create_time, return_selected_day,return_ifrepeat,return_ifremind;
    private MyDatabaseHelper myDatabaseHelper;
    private File output;
    private FileOutputStream fos;
    private File compressedImageFile;
    private File camera_file;
    private File New_Picture_Path;
    private String old_path;
    private Bitmap picture_change;
    private int return_background;
    private boolean isModified;


    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initUI();
        initDate();
        initBackground();
        initSetHeight();
        initSetAnotherViewData();
        picturePath = "";
      //  initPression();
        myDatabaseHelper = new MyDatabaseHelper(getApplicationContext(), "ItemDetails.db", null, 1);
        database = myDatabaseHelper.getWritableDatabase();


        initEditItem();

    }

    private void initPicturePression() {
        if (Build.VERSION.SDK_INT <23){
          //  SpUtils.putBoolean(getApplicationContext(),ConstantValue.CAMERA_ALLOW,true);
            SpUtils.putBoolean(getApplicationContext(), ConstantValue.WRITE_EXTERNAL_STORAGE_ALLOW, true);
        }else {
            Picture_allow();
        }
    }
    private void initCameraPression() {
        if (Build.VERSION.SDK_INT <23){
             SpUtils.putBoolean(getApplicationContext(),ConstantValue.CAMERA_ALLOW,true);
            //SpUtils.putBoolean(getApplicationContext(), ConstantValue.WRITE_EXTERNAL_STORAGE_ALLOW, true);
        }else {
            Camera_Allow();
        }
    }
    private void initEditItem() {
        Intent intent = getIntent();
        if ((request_code = intent.getStringExtra("request_code")).equals("edit_item")) {
            return_title_des = intent.getStringExtra("title_des");
            mTv_title.setText(return_title_des);
            return_selected_day = intent.getStringExtra("selected_day");
            return_create_time = intent.getStringExtra("create_time");
            date_chosed=return_selected_day;
            mTv_date.setText(return_selected_day);
            if (intent.getStringExtra("background").substring(0, 1).equals("#")) {
                selected_color=intent.getStringExtra("background");
                cl_item.setBackgroundColor(Color.parseColor(intent.getStringExtra("background")));
            } else {
                picturePath = intent.getStringExtra("background");
                bitmap = BitmapFactory.decodeFile(picturePath);
                cl_item.setBackground(new BitmapDrawable(bitmap));
            }
            Cursor repeatCursor=database.query("ItemDetails",new String[]{"ifrepeat"},"title_des=? and create_time=?",new String[]{return_title_des, return_create_time},null,null,null);
                    //database.execSQL("select ifrepeat from ItemDetails where title_des='"+return_title_des+"' and create_time='"+return_create_time+"';");
            Cursor remindCursor=database.query("ItemDetails",new String[]{"ifremind"},"title_des=? and create_time=?",new String[]{return_title_des, return_create_time},null,null,null);
            while (repeatCursor.moveToNext())
            tv_if_repeat.setText(repeatCursor.getString(repeatCursor.getColumnIndex("ifrepeat")));
            while (remindCursor.moveToNext())
            tv_if_remind.setText(remindCursor.getString(remindCursor.getColumnIndex("ifremind")));
        }
    }

    private void Picture_allow() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else{
            SpUtils.putBoolean(getApplicationContext(), ConstantValue.WRITE_EXTERNAL_STORAGE_ALLOW, true);
        }
       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }*/
    }
    private void Camera_Allow() {
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }else {
            SpUtils.putBoolean(getApplicationContext(),ConstantValue.CAMERA_ALLOW,true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SpUtils.putBoolean(getApplicationContext(), ConstantValue.WRITE_EXTERNAL_STORAGE_ALLOW, true);
                } else {
                    SpUtils.putBoolean(getApplicationContext(), ConstantValue.WRITE_EXTERNAL_STORAGE_ALLOW, false);
                    Toast.makeText(getApplicationContext(), "您取消了授权，图片背景将不可用。", Toast.LENGTH_SHORT).show();
                }
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SpUtils.putBoolean(getApplicationContext(), ConstantValue.CAMERA_ALLOW, true);
                } else {
                    SpUtils.putBoolean(getApplicationContext(), ConstantValue.CAMERA_ALLOW, false);
                    Toast.makeText(getApplicationContext(), "您取消了授权，拍照功能将不可用。", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void initSetAnotherViewData() {
        mTv_title_des = mTv_title.getText().toString();
        if (mTv_title_des.equals("")) {
            Log.i(tag, "未接收到文字");
        } else {
            Log.i(tag, "接收到的文字是：" + mTv_title_des);
        }
        mTv_anotherView_title = findViewById(R.id.tv_roughly_title);
        if (mTv_title_des.equals("")) {
            mTv_anotherView_title.setText("有一天");
        } else {
            mTv_anotherView_title.setText(mTv_title_des);
        }

        mTv_anotherView_selectedDay = findViewById(R.id.tv_selected_day);
        mTv_anotherView_selectedDay.setText(date_chosed);

        mTv_dayDistance = findViewById(R.id.tv_remaining_day);
        initDayDistance();

    }

    private void initDayDistance() {
        calDayNumber = CalDayUtil.GetDay(dataString, date_chosed);
        Log.i(tag, "相差日期为：" + calDayNumber);

        if (calDayNumber > 0) {
            mTv_dayDistance.setText("还差" + calDayNumber + "天");
        } else if (calDayNumber == 0) {
            mTv_dayDistance.setText("今天");
        } else {
            mTv_dayDistance.setText("已过" + Math.abs(calDayNumber) + "天");
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initBackground() {
        iv_background_color = findViewById(R.id.iv_background_color);
        iv_camera = findViewById(R.id.iv_camera);
        iv_picture = findViewById(R.id.iv_picture);


        iv_background_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this, ColorActivity.class);
                startActivityForResult(intent, 3);

                overridePendingTransition(R.anim.slide_up_in, 0);
            }
        });

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtils.getBoolean(getApplicationContext(), ConstantValue.CAMERA_ALLOW, false)) {
                    /**
                     * 最后一个参数是文件夹的名称，可以随便起
                     */
                    camera_file = new File(Environment.getExternalStorageDirectory(), "takePhotos");
                    if (!camera_file.exists()) {
                        camera_file.mkdir();
                    }
                    /**
                     * 这里将时间作为不同照片的名称
                     */
                    output = new File(camera_file, System.currentTimeMillis() + ".jpeg");

                    /**
                     * 如果该文件夹已经存在，则删除它，否则创建一个
                     */
                    try {
                        if (output.exists()) {
                            output.delete();
                        }
                        output.createNewFile();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /**
                     * 隐式打开拍照的Activity，并且传入CAMERA_REQUEST_CODE常量作为拍照结束后回调的标志
                     * 将文件转化为uri
                     */

                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                    startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
                } else {
                   initCameraPression();
                }
            }
        });

        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpUtils.getBoolean(getApplicationContext(), ConstantValue.WRITE_EXTERNAL_STORAGE_ALLOW, false)) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 101);
                } else {
                    initPicturePression();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initUI() {
        cl_item = findViewById(R.id.ll_1);
        cl_item_all = findViewById(R.id.cl_item_all);
        cl_item_roughly = findViewById(R.id.cl_item_roughly);


        iv_back = findViewById(R.id.iv_add_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(AddItemActivity.this,MainActivity.class);
                startActivity(intent);*/
                AddItemActivity.this.finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        mTv_title = findViewById(R.id.tv_title_des);


        mTv_date = findViewById(R.id.tv_date);
        mTv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Code = 1;
                initDate();
            }
        });

        tv_if_repeat = findViewById(R.id.tv_if_repeat);
        tv_if_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddItemActivity.this, RepeatActivity.class);
                startActivityForResult(intent, 1);

                overridePendingTransition(R.anim.slide_up_in, 0);


            }
        });


        tv_if_remind = findViewById(R.id.tv_if_remind);
        tv_if_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(AddItemActivity.this, RemindActivity.class);
                startActivityForResult(intent2, 2);
                overridePendingTransition(R.anim.slide_up_in, 0);
            }
        });

        iv_see_total = findViewById(R.id.see_total);
        iv_see_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExchangeView();
                initSetAnotherViewData();
            }
        });

        iv_cancel = findViewById(R.id.civ_red_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                show();
            }
        });

        iv_ok = findViewById(R.id.civ_green_ok);
        iv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (request_code.equals("no")) {
                    initSetAnotherViewData();
                    Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                    intent.putExtra("title_des", mTv_anotherView_title.getText().toString());
                    intent.putExtra("selected_day", date_chosed);
                    intent.putExtra("day_distance", "");
                    intent.putExtra("ifremind",tv_if_remind.getText().toString());
                    intent.putExtra("ifrepeat",tv_if_repeat.getText().toString());
                    if (!picturePath.equals("")) {
                        intent.putExtra("background", picturePath);
                    } else {
                        intent.putExtra("background", selected_color);
                    }

                    intent.putExtra("create_time", CalDayUtil.getCreateTime());
                    setResult(104, intent);

                    finish();
                    overridePendingTransition(0, R.anim.fade);
                } else if (request_code.equals("edit_item")) {
                    initSetAnotherViewData();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("title_des", mTv_anotherView_title.getText().toString());
                    contentValues.put("selected_day", mTv_date.getText().toString());
                    if (!picturePath.equals("")) {
                        contentValues.put("background", picturePath);
                    } else {
                        contentValues.put("background",selected_color);
                    }
                //    Log.i(tag, "啦啦啦啦啦啦" + contentValues.get("title_des") + "哈哈哈哈哈" + contentValues.get("background") + "草拟吗啊" + return_create_time);

                    database.update("ItemDetails", contentValues, "title_des =? AND create_time=?", new String[]{return_title_des, return_create_time});
                    database.execSQL("update ItemDetails set ifrepeat='"+tv_if_repeat.getText().toString()+"' where title_des='"+return_title_des+"' and create_time='"+return_create_time+"';");
                    database.execSQL("update ItemDetails set ifremind='"+tv_if_remind.getText().toString()+"' where title_des='"+return_title_des+"' and create_time='"+return_create_time+"';");
                    if (isModified){
                        if (tv_if_remind.getText().toString().equals("不提醒")){
                            database.execSQL("update ItemDetails set remind=_day'"+""+"' where title_des='"+return_title_des+"' and create_time='"+return_create_time+"';");
                        }else if (tv_if_remind.getText().toString().equals("当天提醒")) {
                            database.execSQL("update ItemDetails set remind_day='" + mTv_date.getText().toString() + "' where title_des='" + return_title_des + "' and create_time='" + return_create_time + "';");
                        }else {
                            database.execSQL("update ItemDetails set remind_day='" + CalDayUtil.getformerDate(mTv_date.getText().toString()) + "' where title_des='" + return_title_des + "' and create_time='" + return_create_time + "';");
                        }
                    }
                    startActivity(new Intent(AddItemActivity.this,MainActivity.class));
                    finish();
                    overridePendingTransition(0, R.anim.fade);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (isExit) {
            startActivity(new Intent(AddItemActivity.this,MainActivity.class));
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade);
        } else {
            show();
        }

    }
    private void ExchangeView() {
        if (cl_item_all.getVisibility() == View.VISIBLE) {
            cl_item_all.setVisibility(View.GONE);
            cl_item_roughly.setVisibility(View.VISIBLE);
            iv_see_total.setBackgroundResource(R.drawable.icon_eye_open);
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            cl_item_roughly.setAnimation(animation);
            cl_item_all.setAnimation(animation2);
        } else if (cl_item_all.getVisibility() == View.GONE) {
            cl_item_all.setVisibility(View.VISIBLE);
            cl_item_roughly.setVisibility(View.GONE);
            iv_see_total.setBackgroundResource(R.drawable.icon_eye_close);
            Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
            cl_item_roughly.setAnimation(animation);
            cl_item_all.setAnimation(animation2);
        }
    }
    private void initSetHeight() {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cl_item_roughly.getLayoutParams();
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        cl_item_all.measure(w, h);
        int height = cl_item_all.getMeasuredHeight();
        int width = cl_item_all.getMeasuredWidth();
        params.height = height;
        params.width = width;
        cl_item_roughly.setLayoutParams(params);
        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
     //   Log.i(tag, "百分比为：" + (((float) width1 - (float) width) / 2.0) / (float) width);
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDate() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month + 1 < 10) {
            if (day < 10) {
                dataString = year + "年" + "0" + (month + 1) + "月" + "0" + day + "日";
            } else {
                dataString = year + "年" + "0" + (month + 1) + "月" + day + "日";
            }
        } else {
            if (day < 10) {
                dataString = year + "年" + (month + 1) + "月" + "0" + day + "日";
            } else {
                dataString = year + "年" + (month + 1) + "月" + day + "日";
            }
        }
        if (Code == 1) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    i = datePicker.getYear();
                    i1 = datePicker.getMonth();
                    i2 = datePicker.getDayOfMonth();
                    year1 = i;
                    month1 = i1 + 1;
                    day1 = i2;
                    calDayNumber = CalDayUtil.GetDay(dataString, date_chosed);
                    Log.i(tag, "相差日期为：" + calDayNumber);
                    if (i1 + 1 < 10) {
                        if (i2 < 10) {
                            date_chosed = i + "年" + "0" + (i1 + 1) + "月" + "0" + i2 + "日";
                        } else {
                            date_chosed = i + "年" + "0" + (i1 + 1) + "月" + i2 + "日";
                        }
                    } else {
                        if (i2 < 10) {
                            date_chosed = i + "年" + (i1 + 1) + "月" + "0" + i2 + "日";
                        } else {
                            date_chosed = i + "年" + (i1 + 1) + "月" + i2 + "日";
                        }
                    }
                    mTv_date.setText(date_chosed);
                }
            }, year, month, day);
            datePickerDialog.show();

        } else {
            if (month + 1 < 10) {
                if (day < 10) {
                    date_chosed = year + "年" + "0" + (month + 1) + "月" + "0" + day + "日";
                } else {
                    date_chosed = year + "年" + "0" + (month + 1) + "月" + day + "日";
                }

            } else {
                if (day < 10) {
                    date_chosed = year + "年" + (month + 1) + "月" + "0" + day + "日";
                } else {
                    date_chosed = year + "年" + (month + 1) + "月" + day + "日";
                }

            }
            mTv_date.setText(date_chosed);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpUtils.putInt(getApplicationContext(), ConstantValue.REPEAT_ISCHEAKED, 0x7f070028);
        SpUtils.putInt(getApplicationContext(), ConstantValue.REMIND_ISCHEAKED, 0x7f070024);
        Code = 0;
    }

    @Override
    public void finish() {
        super.finish();
        if (AddItemActivity.this.isFinishing()) {

        }
    }

    public void show() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.cancel_dialog_layout, null);        //初始化控件
        tv_cancel_yes = inflate.findViewById(R.id.tv_cancel_yes);
        tv_cancel_no = inflate.findViewById(R.id.tv_cancel_no);
        tv_cancel_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            /*    startActivity(new Intent(AddItemActivity.this,MainActivity.class));*/
               AddItemActivity.this.finish();
                overridePendingTransition(0, R.anim.fade);

            }
        });
        tv_cancel_no.setOnClickListener(new View.OnClickListener() {
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

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case 1:
                        tv_if_repeat.setText(data.getStringExtra("ifRepeat"));

                        break;
                    case 2:
                        tv_if_repeat.setText(data.getStringExtra("ifRepeat"));
                        break;
                    case 3:
                        tv_if_repeat.setText(data.getStringExtra("ifRepeat"));
                        break;
                }
            case 2:
                isModified=true;
                switch (resultCode) {
                    case 4:
                        tv_if_remind.setText(data.getStringExtra("ifRemind"));
                        break;
                    case 5:
                        tv_if_remind.setText(data.getStringExtra("ifRemind"));
                        break;
                    case 6:
                        tv_if_remind.setText(data.getStringExtra("ifRemind"));
                        break;
                }
            case 3:
                switch (resultCode) {
                    case 7:
                        selected_color = "#F5F5DC";
                        cl_item.setBackgroundColor(Color.parseColor("#F5F5DC"));
                        picturePath = "";
                        break;
                    case 8:
                        selected_color = "#00ff00";
                        cl_item.setBackgroundColor(Color.parseColor("#00ff00"));
                        picturePath = "";
                        break;
                    case 9:
                        selected_color = "#00BFFF";
                        cl_item.setBackgroundColor(Color.parseColor("#00BFFF"));
                        picturePath = "";
                        break;
                    case 10:
                        selected_color = "#FFA500";
                        cl_item.setBackgroundColor(Color.parseColor("#FFA500"));
                        picturePath = "";
                        break;
                    case 11:
                        selected_color = "#FF0000";
                        cl_item.setBackgroundColor(Color.parseColor("#FF0000"));
                        picturePath = "";
                        break;
                }
            case 101:
                if (resultCode == this.RESULT_OK) {
                    //内容解析者来操作内容提供最对数据的4方法
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                            //选择的就只是一张图片，所以cursor只有一条记录
                            if (cursor != null) {
                                if (cursor.moveToFirst()) {

                                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                    cursor.moveToFirst();
                                    ZipPicture zipPicture = new ZipPicture();
                                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                    /*Bitmap bitmap=BitmapFactory.decodeFile(String.valueOf(columnIndex));*/
                                    picturePath = cursor.getString(columnIndex);
                                    if (FileSizeUtil.getFileOrFilesSize(picturePath, 2) > 500) {
                                        zipPicture.zipImageWithOtherQuality(picturePath, 99);
                                        cl_item.setBackground(new BitmapDrawable(picturePath));
                                    } else {
                                        cl_item.setBackground(new BitmapDrawable(picturePath));
                                    }
                                    cursor.close();
                                }
                            }
                        }
                    }

                }
                break;
            case CAMERA_REQUEST_CODE:

                if (resultCode == this.RESULT_OK) {
                    /*startPhotoZoom(Uri.fromFile(output));*/
                    ZipPicture zipPicture = new ZipPicture();
                    zipPicture.zipImage(String.valueOf(output));
                    picturePath = String.valueOf(output);
                    cl_item.setBackground(new BitmapDrawable(picturePath));
                } else if (requestCode == RESULT_CANCELED) {
                    finish();
                }

                break;
            case CROP:
                /*Log.i(tag,"裁剪结果返回intent.data="+data.getData());*/

         /*       Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(output)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                picturePath= String.valueOf(Uri.fromFile(output));
                cl_item.setBackground(new BitmapDrawable(picturePath));
*/


               /* if (data!=null){
                    try {

                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(output)));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }*/
            // bitmap.compress(Bitmap.CompressFormat.JPEG,70,fos);

           /*      picturePath=output.toString();
                cl_item.setBackground(new BitmapDrawable(picturePath));
*/
            break;

        }
    }

    public void startPhotoZoom(Uri uri) {
        Log.i(tag, "开始缩放uri：" + uri);
        cropImageUri(uri, 500, 1000, CROP);
    }

    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);//缩放比例
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", outputX);//输出图片大小
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);//保持比例
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);//裁剪结果存放路径
        intent.putExtra("return-data", false);//是否返回bitmap
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // no face detection
        startActivityForResult(intent, requestCode);
    }
}

