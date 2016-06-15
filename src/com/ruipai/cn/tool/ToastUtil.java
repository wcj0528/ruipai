package com.ruipai.cn.tool;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 项目名称：UtilsLib
 * 作者：lb291
 * 类描述：显示Toast的工具类，提供静态方法调用，禁止实例化该类
 */
public class ToastUtil {

    //===Desc:成员变量==========================================================================================
    /**
     * 默认的TAG信息
     */
    private static final String TAG = "ToastUtil";
    private static Toast toast;

    //===Desc:构造函数==========================================================================================

    /**
     * 禁止实例化该类
     */
    private ToastUtil() {
        throw new UnsupportedOperationException(
                "The class " + getClass().getSimpleName() + " can not be instantiated!");
    }

    //===Desc:提供给外界使用的方法==========================================================================================

    /**
     * 根据isLong显示一个长时间的Toast或者短时间的Toast
     *
     * @param context Context对象
     * @param text    需要Toast显示的信息
     * @param isLong  是否是长时间的Toast，true：长时间。false：短时间的Toast
     */
    public static void showToastByIslong(Context context, String text, boolean isLong) {
        if (TextUtils.isEmpty(text)) {
            LogUtils.e( "The Toast show text is null or \"\"!");
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        if (isLong)
            toast.setDuration(Toast.LENGTH_LONG);
        else
            toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }

    /**
     * 显示一个短时间的Toast信息
     *
     * @param context Context对象
     * @param text    需要显示的文本信息
     */
    public static void showShortToast(Context context, String text) {
        showToastByIslong(context, text, false);
    }

    /**
     * 显示一个长时间的Toast信息
     *
     * @param context Context对象
     * @param text    需要Toast显示的信息
     */
    public static void showLongToast(Context context, String text) {
        showToastByIslong(context, text, true);
    }
}
