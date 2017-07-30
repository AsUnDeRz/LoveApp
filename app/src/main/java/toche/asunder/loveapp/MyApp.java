package toche.asunder.loveapp;

import android.annotation.SuppressLint;
import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by admin on 7/30/2017 AD.
 */

public class MyApp extends Application {


    @SuppressLint("StaticFieldLeak")
    public static Utils typeFace;

    public void onCreate() {
        super.onCreate();

        //utils typeface
        typeFace = new Utils(getApplicationContext());


        //set typeface all text in activity call method
        /*
        override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
        }
         */
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/AvenirNextLTPro-Bold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());




    }
}
