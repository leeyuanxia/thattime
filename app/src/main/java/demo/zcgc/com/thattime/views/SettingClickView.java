package demo.zcgc.com.thattime.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import demo.zcgc.com.thattime.R;


public class SettingClickView extends ConstraintLayout {

    private TextView mTv_des;
    private TextView mTv_title;

    public SettingClickView(Context context) {
        this(context, null);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml--->view  将设置界面转换成一个条目；
        View.inflate(context, R.layout.setting_click_view, this);
       /* ==
       View view=View.inflate(context, R.layout.setting_item_view,this);
        this.addView(view);*/

        //自定义组合空间及其描述
        mTv_title = this.findViewById(R.id.tv_title);
        mTv_des = this.findViewById(R.id.tv_des);

    }

    /**
     * @param title 设置标题内容
     */
    public void setTitle(String title){
        mTv_title.setText(title);
    }

    /**
     * @param des  设置描述内容
     */
    public void setDes(String des){
        mTv_des.setText(des);
    }

}
