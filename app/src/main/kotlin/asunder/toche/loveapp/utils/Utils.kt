package asunder.toche.loveapp

import android.content.Context
import android.graphics.Typeface

/**
 * Created by admin on 8/7/2017 AD.
 */
class Utils(private var context: Context) {

    val heavy: Typeface
        get() = Typeface.createFromAsset(context.assets, "fonts/AvenirLTStd-Heavy.otf")

    val medium: Typeface
        get() = Typeface.createFromAsset(context.assets, "fonts/AvenirLTStd-Medium.otf")


}