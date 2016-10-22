package hksarg.fehd.nutab;


import android.app.Application;

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
    }
}