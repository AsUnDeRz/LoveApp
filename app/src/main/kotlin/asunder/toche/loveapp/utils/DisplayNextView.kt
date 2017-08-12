package asunder.toche.loveapp

import android.view.animation.Animation
import android.widget.ImageButton
import asunder.toche.loveapp.SwapViews



/**
 * Created by admin on 8/10/2017 AD.
 */
open class DisplayNextView(private val mCurrentView: Boolean, internal var image1: ImageButton, internal var image2: ImageButton) : Animation.AnimationListener {

    override fun onAnimationStart(animation: Animation) {}

    override fun onAnimationEnd(animation: Animation) {
        image1.post(SwapViews(mCurrentView, image1, image2))
    }

    override fun onAnimationRepeat(animation: Animation) {}
}