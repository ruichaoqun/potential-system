package com.example.mystudy;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/6/30.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1122/3712.html
         * 在BaseActivity.java里：我们通过判断当前sdk_int大于4.4(kitkat),则通过代码的形式设置status bar为透明
         * (这里其实可以通过values-v19 的sytle.xml里设置windowTranslucentStatus属性为true来进行设置，但是在某些手机会不起效，所以采用代码的形式进行设置)。
         * 还需要注意的是我们这里的AppCompatAcitivity是android.support.v7.app.AppCompatActivity支持包中的AppCompatAcitivity,也是为了在低版本的android系统中兼容toolbar。
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isTranslucentStatusBar()) {
                Window window = getWindow();
                // Translucent status bar
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        setContentView(getLayoutId());
        initResAndListener();
    }

    //是否statusBar 状态栏为透明 的方法 默认为真
    protected boolean isTranslucentStatusBar() {
        return true;
    }

    abstract void initResAndListener();
    abstract int getLayoutId();
}
