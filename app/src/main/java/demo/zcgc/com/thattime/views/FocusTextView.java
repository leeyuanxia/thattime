package demo.zcgc.com.thattime.views;

import android.content.Context;
import android.util.AttributeSet;

/*能够获取焦点的自定义TextView*/
public class FocusTextView extends android.support.v7.widget.AppCompatTextView {
    //使用通过Java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }
    //带属性加上下文环境的构造方法
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //以及在布局文件中定义的样式的构建方法
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //重写获取焦点的方法
    @Override
    public boolean isFocused() {
        return true;
    }
}
