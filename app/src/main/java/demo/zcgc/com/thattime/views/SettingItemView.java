package demo.zcgc.com.thattime.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import demo.zcgc.com.thattime.R;


public class SettingItemView extends ConstraintLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/demo.zcgc.com.thattime";
    private CheckBox mCb_box;
    private TextView mTv_title;
    private TextView mTv_des;

    private String mDestitle;
    private String mDeson;
    private String mDesoff;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml--->view  将设置界面转换成一个条目；
        View.inflate(context, R.layout.setting_item_view, this);
       /* ==
       View view=View.inflate(context, R.layout.setting_item_view,this);
        this.addView(view);*/

        //自定义组合空间及其描述
        mTv_title = this.findViewById(R.id.tv_title);
        mTv_des = this.findViewById(R.id.tv_des);
        mCb_box = this.findViewById(R.id.cb_box);

        //获取自定义以及原生属性的操作，写在次处AttributeSet attrs
        initAttrs(attrs);

        mTv_title.setText(mDestitle);
    }

    /**
     * 构造方法中维护好的属性集合
     */

    private void initAttrs(AttributeSet attrs) {
      /*  //获取属性名称以及属性值
        for (int i=0;i<attrs.getAttributeCount();i++){
            attrs.getAttributeName(i);
        }*/
      mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
      mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
      mDeson = attrs.getAttributeValue(NAMESPACE, "deson");


    }

    /*判断是否开启*/
    public boolean isCheak() {
        return mCb_box.isChecked();
    }

    /*切换选中状态*/
    public void setCheck(Boolean isCheck) {
        //当前条目在选择的过程中，mCb_box选中状态也变化
        mCb_box.setChecked(isCheck);
        if (isCheck) {
            //开启
            mTv_des.setText(mDeson);
        } else {
            mTv_des.setText(mDesoff);
        }
    }

}
