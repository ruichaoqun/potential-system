package base;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Administrator on 2016/6/29.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        //设置Vector矢量图可以从资源文件获取
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
