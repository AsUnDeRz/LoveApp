package view.custom_view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

/**
 * Created by admin on 8/7/2017 AD.
 */
class TextViewMedium : TextView {

    constructor(context: Context) : super(context){
        applyFont(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        applyFont(context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr){
        applyFont(context)
    }

    private fun applyFont(context: Context) {
        val customFont = Typeface.createFromAsset(context.assets, "fonts/AvenirLTStd-Medium.otf")
        typeface = customFont
    }
}