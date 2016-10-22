package com.xdroid.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

/**
 * This class is used to change your application locale and persist this change for the next time
 * that your app is going to be used.
 * <p/>
 * You can also change the locale of your application on the fly by using the setLocale method.
 * <p/>
 * Created by gunhansancar on 07/10/15.
 */
public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String SELECTED_COUNTRY = "Locale.Helper.Selected.Country";

    public static void onCreate(Context context) {
        Locale preset = getPersistedData(context);
        setLocale(context, preset);
    }

    public static void setLocale(Context context, Locale locale) {
        persist(context, locale);
        updateResources(context, locale);
    }

    private static Locale getPersistedData(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String szLanguage = preferences.getString(SELECTED_LANGUAGE, Locale.getDefault().getLanguage());
        String szCountry = preferences.getString(SELECTED_COUNTRY, Locale.getDefault().getCountry());
        return new Locale(szLanguage, szCountry);
    }

    private static void persist(Context context, Locale locale) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, locale.getLanguage());
        editor.putString(SELECTED_COUNTRY, locale.getCountry());
        editor.apply();
    }

    private static void updateResources(Context context, Locale locale) {
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}