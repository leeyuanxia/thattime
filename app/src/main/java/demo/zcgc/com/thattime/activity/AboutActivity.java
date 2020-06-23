package demo.zcgc.com.thattime.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.uitls.StreamUtil;

public class AboutActivity extends AppCompatActivity{

    private TextView tv_version,tv_suggest,tv_check_update;
    private ImageView iv_back;
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
    private  int mLocalVersionCode;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //弹出对话框提示更新
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;
                case ENTER_HOME://activity跳转过程。
                    Toast.makeText(AboutActivity.this,"已经是最新版本！",Toast.LENGTH_SHORT).show();
                    break;
                case URL_ERROR:
                    Toast.makeText(AboutActivity.this, "Url异常", Toast.LENGTH_SHORT).show();

                    break;
                case IO_ERROR:
                    Toast.makeText(AboutActivity.this, "读取异常", Toast.LENGTH_SHORT).show();

                    break;
                case JSON_ERROR:
                    Toast.makeText(AboutActivity.this, "JSON解析异常", Toast.LENGTH_SHORT).show();

                    break;

            }
        }
    };
    private String tag="AboutActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initUI();

       initDate();

    }
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
    private void initDate() {
        tv_version.setText("版本号："+getVersionName());
        mLocalVersionCode = getVersionCode();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AboutActivity.this,MoreActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });
        tv_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AboutActivity.this,SuggestActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        tv_check_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVersion();
            }
        });
    }

    private void showUpdateDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AboutActivity.this);
        alertDialog.setIcon(R.drawable.ic_update_black_24dp);//左上角图标
        alertDialog.setTitle("发现新版本！");
        alertDialog.setMessage(mVersionDes);
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
                Toast.makeText(AboutActivity.this,"您取消了更新",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //点击后退也进入
                Toast.makeText(AboutActivity.this,"您取消了更新",Toast.LENGTH_SHORT).show();
            }
        });
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
    private void downloadApk() {
        //1,链接地址，放置apk所在路径
        //1,判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Someday_beta_1.0.005_1810251200.apk";
            //发送请求
            // 获取APK并放置到指定路径；
            mProgressDialog = new ProgressDialog(AboutActivity.this);
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
                    mProgressDialog.dismiss();
                    finish();
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i(tag, "下载失败");
                    Toast.makeText(AboutActivity.this, "下载失败，请重新下载", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.i(tag, "取消下载");
                    mProgressDialog.dismiss();
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
    private void initUI() {
        tv_version=findViewById(R.id.tv_version);
        iv_back=findViewById(R.id.iv_about_back);
        tv_check_update=findViewById(R.id.tv_check_update);

        tv_suggest=findViewById(R.id.tv_suggestes);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AboutActivity.this,MoreActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
    }

    /**
     * 获取版本名称：APP文件中
     *
     * @return 应用版本名称 返回null代表异常
     */

    private String getVersionName() {
        //1,包管理者对象。
        PackageManager pm = getPackageManager();
        //2,，从包管理者对象中获取制定包名的基本信息（版本名称）
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
