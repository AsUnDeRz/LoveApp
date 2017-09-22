package utils.localization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.widget.ProgressBar;

import com.akexorcist.localizationactivity.BlankDummyActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 5/5/2017 AD.
 */

public class LocalDelegate {

    private static final String KEY_ACTIVIY_LOCALE_CHANGED = "activity_locale_changed";

    // Boolean flag to check that activity was recreated from locale changed.
    ProgressBar progressBar;
    private boolean isLocalizationChanged = false;

    // Prepare default language.
    private String currentLanguage = LangSetting.getDefaultLanguage();

    private final Activity activity;
    private final List<OnLocaleChange> localeChangedListeners = new ArrayList<>();

    public LocalDelegate(Activity activity) {
        this.activity = activity;
    }

    public void addOnLocaleChengedListener(OnLocaleChange onLocaleChangedListener) {
        localeChangedListeners.add(onLocaleChangedListener);
    }

    public void onCreate(Bundle savedInstanceState) {
        setupLanguage();
        checkBeforeLocaleChanging();
    }

    // Provide method to set application language by country name.
    public final void setLanguage(String language) {
        if (!isDuplicatedLanguageSetting(language)) {
            LangSetting.setLanguage(activity, language);
            notifyLanguageChanged();
        }
    }

    // Provide method to set application language by locale.
    public final void setLanguage(Locale locale) {
        setLanguage(locale.getLanguage());
    }

    public final void setDefaultLanguage(String language) {
        LangSetting.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        LangSetting.setDefaultLanguage(locale.getLanguage());
    }

    // Get current language
    public final String getLanguage() {
        return LangSetting.getLanguage();
    }

    // Get current locale
    public final Locale getLocale() {
        return LangSetting.getLocale(activity);
    }

    // Check that bundle come from locale change.
    // If yes, bundle will obe remove and set boolean flag to "true".
    private void checkBeforeLocaleChanging() {
        boolean isLocalizationChanged = activity.getIntent().getBooleanExtra(KEY_ACTIVIY_LOCALE_CHANGED, false);
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true;
            activity.getIntent().removeExtra(KEY_ACTIVIY_LOCALE_CHANGED);
        }
    }

    // Setup language to locale and language preference.
    // This method will called before onCreate.
    private void setupLanguage() {
        Locale locale = LangSetting.getLocale(activity);
        setupLocale(locale);
        currentLanguage = locale.getLanguage();
        LangSetting.setLanguage(activity, locale.getLanguage());
    }

    // Set locale configuration.
    private void setupLocale(Locale locale) {
        updateLocaleConfiguration(activity, locale);
    }

    private void updateLocaleConfiguration(Context context, Locale locale) {
        Configuration config = context.getResources().getConfiguration();
        //config.locale = locale;

        config.setLocale(locale);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        context.getResources().updateConfiguration(config, dm);





    }

    // Avoid duplicated setup
    private boolean isDuplicatedLanguageSetting(String language) {
        return language.toLowerCase(Locale.getDefault()).equals(LangSetting.getLanguage());
    }

    // Let's take it change! (Using recreate method that available on API 11 or more.
    private void notifyLanguageChanged() {
        for (OnLocaleChange changedListener : localeChangedListeners) {
            changedListener.onBeforeLocaleChanged();
        }
        activity.getIntent().putExtra(KEY_ACTIVIY_LOCALE_CHANGED, true);




        //callDummyActivity();
        //activity.recreate();
    }

    // If activity is run to backstack. So we have to check if this activity is resume working.
    public void onResume() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkLocaleChange();
                checkAfterLocaleChanging();
            }
        });
    }

    // Check if locale has change while this activity was run to backstack.
    private void checkLocaleChange() {
        if (!LangSetting.getLanguage().toLowerCase(Locale.getDefault())
                .equals(currentLanguage.toLowerCase(Locale.getDefault()))) {
            //activity.recreate();


        }
    }

    // Call override method if local is really changed
    private void checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            for (OnLocaleChange listener : localeChangedListeners) {
                listener.onAfterLocaleChanged();
            }
            isLocalizationChanged = false;
        }
    }

    private void callDummyActivity() {
        activity.startActivity(new Intent(activity, BlankDummyActivity.class));
    }


}
