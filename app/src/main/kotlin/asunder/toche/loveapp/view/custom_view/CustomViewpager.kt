package view.custom_view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by admin on 8/7/2017 AD.
 */
class CustomViewpager(context: Context?, attrs: AttributeSet?) : ViewPager(context, attrs) {

    enum class SwipeDirection{
        all, left, right, none
    }

    var initialXValue:Float = 0.0f
    var direction :SwipeDirection = SwipeDirection.all


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if(this.IsSwipeAllowed(ev)){
            return super.onTouchEvent(ev)

        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if(this.IsSwipeAllowed(ev)){
            return super.onInterceptTouchEvent(ev)

        }
        return false
    }

    fun IsSwipeAllowed(event: MotionEvent?) : Boolean {
        if(this.direction == SwipeDirection.all) return true;

        if(direction == SwipeDirection.none )//disable any swipe
            return false

        if(event!!.action == MotionEvent.ACTION_DOWN) {
            initialXValue = event.x
            return true
        }

        if(event.action == MotionEvent.ACTION_MOVE) {
            try {
                val diffX = event.x - initialXValue;
                if (diffX > 0 && direction == SwipeDirection.right ) {
                    // swipe from left to right detected
                    return false
                }else if (diffX < 0 && direction == SwipeDirection.left ) {
                    // swipe from right to left detected
                    return false
                }
            } catch (exception : Exception) {
                exception.printStackTrace()
            }
        }

        return true;
    }

    fun setAllowedSwipeDirection(direction: SwipeDirection) {
        this.direction = direction
    }

    init {
        this.direction = SwipeDirection.all
    }


}