package asunder.toche.loveapp

import android.support.v4.view.ViewCompat
import android.view.MotionEvent
import android.support.annotation.NonNull
import android.widget.ImageView.ScaleType
import android.databinding.adapters.ViewBindingAdapter.setPadding
import android.view.ViewGroup
import android.opengl.ETC1.getWidth
import android.os.Build
import android.annotation.TargetApi
import android.support.v4.content.res.TypedArrayUtils.getResourceId
import android.content.res.TypedArray
import android.R.drawable.star_off
import android.R.drawable.star_on
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout



/**
 * Created by admin on 8/13/2017 AD.
 */
class CustomRatingBar : LinearLayout {

    private var mMaxStars = 5
    private var mCurrentScore = 2.5f
    private var mStarOnResource = R.drawable.star_blue
    private var mStarOffResource = R.drawable.star_white
    private var mStarHalfResource = R.drawable.star_blue
    lateinit var mStarsViews: Array<ImageView?>
    private var mStarPadding: Float = 0.toFloat()
    var onScoreChanged: IRatingBarCallbacks? = null
    private var mLastStarId: Int = 0
    private var mOnlyForDisplay: Boolean = false
    private var mLastX: Double = 0.toDouble()
    var isHalfStars = true

    var score: Float
        get() = mCurrentScore
        set(score) {
            var score = score
            score = Math.round(score * 2) / 2.0f
            if (!isHalfStars)
                score = Math.round(score).toFloat()
            mCurrentScore = score
            refreshStars()
        }

    interface IRatingBarCallbacks {
        fun scoreChanged(score: Float)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    fun setScrollToSelect(enabled: Boolean) {
        mOnlyForDisplay = !enabled
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeAttributes(attrs, context)
        init()
    }

    private fun initializeAttributes(attrs: AttributeSet, context: Context) {
        val a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomRatingBar)
        val N = a.indexCount
        for (i in 0..N - 1) {
            val attr = a.getIndex(i)
            if (attr == R.styleable.CustomRatingBar_maxStars)
                mMaxStars = a.getInt(attr, 5)
            else if (attr == R.styleable.CustomRatingBar_stars)
                mCurrentScore = a.getFloat(attr, 2.5f)
            else if (attr == R.styleable.CustomRatingBar_starHalf)
                mStarHalfResource = a.getResourceId(attr, android.R.drawable.star_on)
            else if (attr == R.styleable.CustomRatingBar_starOn)
                mStarOnResource = a.getResourceId(attr, android.R.drawable.star_on)
            else if (attr == R.styleable.CustomRatingBar_starOff)
                mStarOffResource = a.getResourceId(attr, android.R.drawable.star_off)
            else if (attr == R.styleable.CustomRatingBar_starPadding)
                mStarPadding = a.getDimension(attr, 0f)
            else if (attr == R.styleable.CustomRatingBar_onlyForDisplay)
                mOnlyForDisplay = a.getBoolean(attr, false)
            else if (attr == R.styleable.CustomRatingBar_halfStars)
                isHalfStars = a.getBoolean(attr, true)
        }
        a.recycle()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initializeAttributes(attrs, context)
        init()
    }

    internal fun init() {
        mStarsViews = arrayOfNulls<ImageView>(mMaxStars)
        for (i in 0 until mMaxStars) {
            val v = createStar()
            addView(v)
            mStarsViews[i] = v
        }
        refreshStars()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return true
    }

    /**
     * hardcore math over here
     *
     * @param x
     * @return
     */
    private fun getScoreForPosition(x: Float): Float {
        if (isHalfStars)
            return Math.round(x / (width.toFloat() / (mMaxStars * 3f)) / 3f * 2f).toFloat() / 2
        val value = Math.round(x / (width.toFloat() / mMaxStars)).toFloat()
        return if (value < 0) 1f else value
    }

    private fun getImageForScore(score: Float): Int {
        return if (score > 0)
            Math.round(score) - 1
        else
            -1
    }

    private fun refreshStars() {
        val flagHalf = mCurrentScore != 0f && mCurrentScore % 0.5 == 0.0 && isHalfStars
        for (i in 1..mMaxStars) {

            if (i <= mCurrentScore)
                mStarsViews[i - 1]?.setImageResource(mStarOnResource)
            else {
                if (flagHalf && i - 0.5 <= mCurrentScore)
                    mStarsViews[i - 1]?.setImageResource(mStarHalfResource)
                else
                    mStarsViews[i - 1]?.setImageResource(mStarOffResource)
            }
        }
    }

    private fun createStar(): ImageView {
        val v = ImageView(context)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.weight = 1f
        v.setPadding(mStarPadding.toInt(), 0, mStarPadding.toInt(), 0)
        v.adjustViewBounds = true
        v.scaleType = ImageView.ScaleType.FIT_CENTER
        v.layoutParams = params
        v.setImageResource(mStarOffResource)
        return v
    }

    private fun getImageView(position: Int): ImageView? {
        try {
            return mStarsViews!![position]
        } catch (e: Exception) {
            return null
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var lastscore :Float
        if (mOnlyForDisplay)
            return true
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                animateStarRelease(getImageView(mLastStarId))
                mLastStarId = -1
            }
            MotionEvent.ACTION_MOVE -> {
                if (Math.abs(event.x - mLastX) > 50)
                    requestDisallowInterceptTouchEvent(true)
                lastscore = mCurrentScore
                mCurrentScore = getScoreForPosition(event.x)
                if (lastscore != mCurrentScore) {
                    animateStarRelease(getImageView(mLastStarId))
                    animateStarPressed(getImageView(getImageForScore(mCurrentScore)))
                    mLastStarId = getImageForScore(mCurrentScore)
                    refreshStars()
                    if (onScoreChanged != null)
                        onScoreChanged!!.scoreChanged(mCurrentScore)
                }
            }
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x.toDouble()
                lastscore = mCurrentScore
                mCurrentScore = getScoreForPosition(event.x)
                animateStarPressed(getImageView(getImageForScore(mCurrentScore)))
                mLastStarId = getImageForScore(mCurrentScore)
                if (lastscore != mCurrentScore) {
                    refreshStars()
                    if (onScoreChanged != null)
                        onScoreChanged!!.scoreChanged(mCurrentScore)
                }
            }
        }
        return true
    }

    private fun animateStarPressed(star: ImageView?) {
        if (star != null)
            ViewCompat.animate(star).scaleX(1.2f).scaleY(1.2f).setDuration(100).start()
    }

    private fun animateStarRelease(star: ImageView?) {
        if (star != null)
            ViewCompat.animate(star).scaleX(1f).scaleY(1f).setDuration(100).start()
    }
}