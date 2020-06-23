package demo.zcgc.com.thattime.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import demo.zcgc.com.thattime.uitls.CalDayUtil;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    public static final String CREATE_ItemDetails = "create table ItemDetails ("

            + "id integer primary key autoincrement, "

            + "title_des string,"

            + "day_distance string, "

            + "selected_day string, "

            + "background string, "

            + "ifremind string,"

            + "ifrepeat string ,"

            +"if_imp string,"

            +"remind_day string,"

           +"create_time string)";


    private Context mContext;


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);

        mContext = context;

    }


    @Override

    public void onCreate(SQLiteDatabase db) {


        db.execSQL(CREATE_ItemDetails);

        /*Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
        Log.i(String.valueOf(mContext), "创建成功");*/

    }


    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}