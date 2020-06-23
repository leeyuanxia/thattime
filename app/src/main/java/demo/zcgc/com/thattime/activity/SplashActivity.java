package demo.zcgc.com.thattime.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import demo.zcgc.com.thattime.Application.MyApplication;
import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;
import demo.zcgc.com.thattime.uitls.StreamUtil;

public class SplashActivity extends AppCompatActivity {
    private static final int INSTALL_APK_REQUESTCODE = 105;
    private static final int GET_UNKNOWN_APP_SOURCES = 106;
    private String mVersionDes;
    /*
    更新新版本的状态码
    */
    private static final int UPDATE_VERSION = 100;
    /*进入程序主界面的状态码*/
    private static final int ENTER_HOME = 101;
    /*UEL地址出错状态码*/
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private ProgressDialog mProgressDialog;
    private String mDownloadUrl;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //弹出对话框提示更新
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME://activity跳转过程。
                    enterHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(SplashActivity.this, "Url异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case IO_ERROR:
                    Toast.makeText(SplashActivity.this, "读取异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "JSON解析异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

            }
        }
    };
    private String tag = "SplashActivity";
    private int mLocalVersionCode;
    private MyDatabaseHelper myDatabaseHelper;
    private ContentValues contentvalues;
    private String dataString;
    private SQLiteDatabase db;
    private int id;
    private String apk_path;
    private String path;

    private void showUpdateDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
        alertDialog.setIcon(R.drawable.ic_update_black_24dp);//左上角图标
        alertDialog.setTitle("发现新版本！");
        alertDialog.setMessage(mVersionDes);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("update", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载APK，地址downloadUrl
                downloadApk();
            }
        });
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消对话框
                enterHome();

            }
        });
        alertDialog.show();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //点击后退也进入
                enterHome();

            }
        });
    }

    private void downloadApk() {
        //1,链接地址，放置apk所在路径
        //1,判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
             path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Someday.apk";
            //发送请求
            // 获取APK并放置到指定路径；
            mProgressDialog = new ProgressDialog(SplashActivity.this);
            RequestParams requestParams = new RequestParams(mDownloadUrl);
            // 为RequestParams设置文件下载后的保存路径
            requestParams.setSaveFilePath(path);
            // 下载完成后自动为文件命名
            requestParams.setAutoRename(true);
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Log.i(tag, "下载成功");
                   Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                    startActivity(intent);
                //startInstall(result);
                mProgressDialog.dismiss();
                finish();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i(tag, "下载失败"+ex);
                    Toast.makeText(SplashActivity.this, "下载失败，请重新下载"+ex, Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    enterHome();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.i(tag, "取消下载");
                    mProgressDialog.dismiss();
                    enterHome();
                }

                @Override
                public void onFinished() {
                    Log.i(tag, "结束下载");
                    mProgressDialog.dismiss();
                }

                @Override
                public void onWaiting() {
                    // 网络请求开始的时候调用
                    Log.i(tag, "等待下载");
                }

                @Override
                public void onStarted() {
                    // 下载的时候不断回调的方法
                    Log.i(tag, "开始下载");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    // 当前的下载进度和文件总大小
                    Log.i(tag, "正在下载中...");

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setMessage("正在下载中...");
                    mProgressDialog.setProgressNumberFormat("%1d Mb/%2d Mb");
                    mProgressDialog.setMax(((int) total / 1024) / 1024);
                    mProgressDialog.setProgress(((int) current / 1024) / 1024);
                    mProgressDialog.show();
                }
            });
        }
    }
    @RequiresApi (api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context){
        if (context == null){
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }

    private void startInstall(File result) {
        apk_path=String.valueOf(Uri.fromFile(result));
        if (Build.VERSION.SDK_INT >= 26) {
            //来判断应用是否有权限安装apk
            boolean installAllowed= getPackageManager().canRequestPackageInstalls();
            //有权限
            if (installAllowed) {
                //安装apk
                install(path);
            } else {
                //无权限 申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_APK_REQUESTCODE);
            }
        } else {
            install(String.valueOf(Uri.fromFile(result)));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case INSTALL_APK_REQUESTCODE:
                //有注册权限且用户允许安装
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    install(path);
                } else {
                    //将用户引导至安装未知应用界面。
                    /*Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);*/
                    install(path);
                }
                break;
        }
    }
    private void install(String apkPath) {
        //7.0以上通过FileProvider
        if (Build.VERSION.SDK_INT >=24) {
            Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileProvider", new File(apkPath));
            Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(uri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getApplicationContext().startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse(apkPath), "application/vnd.android.package-archive");
            getApplicationContext().startActivity(intent);
        }
    }
    /*进入应用程序主界面*/
    private void enterHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        //开启新的界面后将导航界面关闭；
        finish();
        if (!isDestroyed()){
            Log.i(tag,"页面被结束");
        }
    }

    private void initDate() {
        //1,应用版本名称
        mLocalVersionCode = getVersionCode();
        //3，获取服务器版本号（客户端发请求，服务端响应，（json,xml））
        //http://www.zhongchuanggc.com/updat.json?key=value  返回200请求成功，流的方式将数据读取下来。
        //json中内容包含：
        /*更新版本的名称，，新版本的描述信息，，服务端版本号，，下载地址*/
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        } else {
            /*enterHome();*/
            //消息机制,发送消息4秒后处理消息
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);
        }

        dataString=CalDayUtil.GetSystemDate();
        myDatabaseHelper=new MyDatabaseHelper(this,"ItemDetails.db",null,1);
        db=myDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("ItemDetails", null, null, null, null, null, null, null);
        //遍历Curosr对象，取出数据并打印
        while (cursor.moveToNext()) {
            id=cursor.getInt(cursor.getColumnIndex("id"));
        }
        if (id>=1){
            Log.i(tag,"不用添加");
        }else {
            contentvalues=new ContentValues();
        contentvalues.put("title_des","有一天App开始编写");
        contentvalues.put("day_distance","");
        contentvalues.put("selected_day", "2018年09月07日");
        contentvalues.put("background","#FFA500");
        contentvalues.put("if_imp","no");
        contentvalues.put("create_time",CalDayUtil.getCreateTime());

        db.insert("ItemDetails",null,contentvalues);
        }

    }
    /**
     * 返回版本号
     * @return 非0，代表获取成果
     */
    public int getVersionCode() {
        //1,包管理者对象。
        PackageManager pm = getPackageManager();
        //2,，从包管理者对象中获取制定包名的基本信息（版本名称）
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private void checkVersion() {
        new Thread() {
            public void run() {
                //发送请求获取数据，参数则为请求json的链接地址
                //"http://:192.168.1.102/update.json" 不是最好的
                /*10.0.2.2仅限于模拟器访问*/

                Message mMessage = Message.obtain();
                Long starTime = System.currentTimeMillis();
                try {
                    //1，封装URL地址
                    URL url = new URL("http://39.106.20.0:8080/update.json");
                    //2，开启连接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3,设置常见参数（请求头)
                    // 请求超时
                    connection.setConnectTimeout(2000);
                    // 读取超时
                    connection.setReadTimeout(2000);
                    //默认就是get请求方式
                    /*connection.setRequestMethod();*/
                    //4，获取响应码
                    if (connection.getResponseCode() == 200) {
                        //5.以流的形式将数据获取
                        InputStream inputStream = connection.getInputStream();
                        //6，流转字符串（封装）
                        String json = StreamUtil.streamToString(inputStream);
                        //7,json解析
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        //debug
                        Log.i(tag, versionCode);
                        Log.i(tag, mVersionDes);
                        Log.i(tag, versionName);
                        Log.i(tag, mDownloadUrl);

                        //比对版本号
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            //提示用户更新，消息机制
                            mMessage.what = UPDATE_VERSION;
                        } else {
                            //进入应用主界面
                            mMessage.what = ENTER_HOME;
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mMessage.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    mMessage.what = IO_ERROR;

                } catch (JSONException e) {
                    e.printStackTrace();
                    mMessage.what = JSON_ERROR;

                } finally {
                    //指定睡眠时间，请求网络时间长超过4秒则不处理。小于4秒强制4秒；
                    long endTime = System.currentTimeMillis();
                    if (endTime - starTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - starTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(mMessage);
                }
            }

        }.start();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initDate();

    }
}