package toche.asunder.loveapp;


import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by admin on 7/30/2017 AD.
 */

public class   Utils {

    public Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public Typeface getHeavy(){
        return Typeface.createFromAsset(context.getAssets(), "fonts/AvenirLTStd-Heavy.otf");
    }

    public Typeface getMedium(){
        return Typeface.createFromAsset(context.getAssets(), "fonts/AvenirLTStd-Medium.otf");

    }



}
