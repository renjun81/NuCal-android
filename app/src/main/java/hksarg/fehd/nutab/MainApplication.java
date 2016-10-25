package hksarg.fehd.nutab;


import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.xdroid.widget.LocaleHelper;

import java.util.Locale;

/**
 * When your application is launched this class is loaded before all of your activies.
 * And the instance of this class will live through whole application lifecycle.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LocaleHelper.onCreate(this);
        ActiveAndroid.initialize(this);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        Log.e("###", "Screen density = " + dm.density + ", " + dm.densityDpi + ", " + dm.scaledDensity);
        Log.e("###", "Screen size(px) = " + dm.widthPixels + " x " + dm.heightPixels);
        Log.e("###", "Screen size(dp) = " + dm.xdpi + " x " + dm.ydpi);
    }
}