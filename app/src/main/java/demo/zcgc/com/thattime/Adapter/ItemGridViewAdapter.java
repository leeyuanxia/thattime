package demo.zcgc.com.thattime.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import demo.zcgc.com.thattime.R;

public class ItemGridViewAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private List<Integer> list;
    private  Context context;
    private LayoutInflater layoutInflater;
    private View ItemGridView;
    private TextView tv_rougly_title,tv_remining_day,tv_selected_day;

    public ItemGridViewAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View view1 = View.inflate(context, R.layout.grid_item_view, null);
     /*   AbsListView.LayoutParams param = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ItemHeight());
        view1.setLayoutParams(param);
        tv_rougly_title = view1.findViewById(R.id.tv_roughly_title);
        tv_remining_day = view1.findViewById(R.id.tv_remaining_day);
        tv_selected_day = view1.findViewById(R.id.tv_selected_day);*/

         /*   tv_rougly_title.setText("有一天App开始编写");
            tv_remining_day.setText("已过" + CalDayUtil.GetDay("2018年09月07日", dataString)+"天");
            tv_selected_day.setText("2018年09月07日");*/

        return view1;

    }
/*    private int ItemHeight() {
        int itemheight = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        *//*int width = wm.getDefaultDisplay().getWidth();*//*
        int height = wm.getDefaultDisplay().getHeight();
        itemheight = (int) (height * 0.38);
        return itemheight;
    }*/
}
