package asunder.toche.loveapp

/**
 * Created by admin on 8/10/2017 AD.
 */
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView

class SwapViews(private val mIsFirstView: Boolean, internal var image1: ImageButton, internal var image2: ImageButton) : Runnable {

    override fun run() {
        val centerX = image1.width / 2.0f
        val centerY = image1.height / 2.0f
        val rotation: Flip3d

        if (mIsFirstView) {
            image1.visibility = View.GONE
            image2.visibility = View.VISIBLE
            image2.requestFocus()

            rotation = Flip3d(-90f, 0f, centerX, centerY)
        } else {
            image2.visibility = View.GONE
            image1.visibility = View.VISIBLE
            image1.requestFocus()

            rotation = Flip3d(90f, 0f, centerX, centerY)
        }

        rotation.duration = 500
        rotation.fillAfter = true
        rotation.interpolator = DecelerateInterpolator()

        if (mIsFirstView) {
            image2.startAnimation(rotation)
        } else {
            image1.startAnimation(rotation)
        }
    }
}