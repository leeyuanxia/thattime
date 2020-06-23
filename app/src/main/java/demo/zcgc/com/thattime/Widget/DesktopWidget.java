package demo.zcgc.com.thattime.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.RemoteViews;

import demo.zcgc.com.thattime.R;
import demo.zcgc.com.thattime.engine.MyDatabaseHelper;
import demo.zcgc.com.thattime.uitls.CalDayUtil;
import demo.zcgc.com.thattime.uitls.ConstantValue;
import demo.zcgc.com.thattime.uitls.SpUtils;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link DesktopWidgetConfigureActivity DesktopWidgetConfigureActivity}
 */
public class DesktopWidget extends AppWidgetProvider {
    private static MyDatabaseHelper myDatabaseHelper;
    private static SQLiteDatabase database;
    private String title_des,create_time;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("小插件日志","title_des="+intent.getStringExtra("title_des"));
        title_des=intent.getStringExtra("title_des");
        create_time=intent.getStringExtra("create_time");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.tv_widget_title,title_des);
        views.setTextViewText(R.id.tv_widget_reminday_day,create_time);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        myDatabaseHelper = new MyDatabaseHelper(context, "ItemDetails.db", null, 1);
        database = myDatabaseHelper.getWritableDatabase();
        Log.i("插件-------------->","TETLE_DES="+SpUtils.getString(context, ConstantValue.DESKTOP_TITLE_DES,""));
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
       Cursor cursor= database.rawQuery("select * from ItemDetails where title_des=? and create_time=?",
                new String[]{SpUtils.getString(context, ConstantValue.DESKTOP_TITLE_DES,""),
                        SpUtils.getString(context, ConstantValue.DESKTOP_CREATE_TIME,"")});
       while (cursor.moveToNext()){
           views.setTextViewText(R.id.tv_widget_title,cursor.getString(cursor.getColumnIndex("title_des")));
           views.setTextViewText(R.id.tv_widget_reminday_day,CalDayUtil.distance_day(cursor.getString(cursor.getColumnIndex("selected_day"))));
           views.setTextViewText(R.id.tv_widget_select_day,cursor.getString(cursor.getColumnIndex("selected_day")));

       }
       database.close();
        /*views.setTextViewText(R.id.tv_widget_title,SpUtils.getString(context, ConstantValue.DESKTOP_TITLE_DES,""));
        views.setTextViewText(R.id.tv_widget_reminday_day,SpUtils.getString(context, ConstantValue.DESKTOP_CREATE_TIME,""));
        views.setTextViewText(R.id.tv_widget_select_day,"s选择日期");*/
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            DesktopWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

