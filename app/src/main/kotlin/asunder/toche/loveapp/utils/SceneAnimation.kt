package asunder.toche.loveapp

import android.view.View
import android.widget.ImageView


/**
 * Created by ToCHe on 8/30/2017 AD.
 */

class SceneAnimation(pImageView: ImageView, pFrameRess: IntArray, pDuration: Int, pBreakDelay: Long,view: View) {
    private var mImageView: ImageView? = null
    private var mFrameRess: IntArray? = null
    private lateinit var mDurations: IntArray
    private var mDuration: Int = 0
    private var viewLoading : View? = null

    private var mLastFrameNo: Int = 0
    private var mBreakDelay: Long = 0

    init {
        mImageView = pImageView
        mFrameRess = pFrameRess
        mDuration = pDuration
        mLastFrameNo = pFrameRess.size - 1
        mBreakDelay = pBreakDelay
        viewLoading = view

        mImageView!!.setImageResource(mFrameRess!![0])
        playConstant(1)
    }


    private fun play(pFrameNo: Int) {
        mImageView!!.postDelayed({
            mImageView!!.setImageResource(mFrameRess!![pFrameNo])
            if (pFrameNo == mLastFrameNo)
                play(0)
            else
                play(pFrameNo + 1)
        }, mDuration.toLong())
    }


    private fun playConstant(pFrameNo: Int) {
        mImageView!!.postDelayed(Runnable {
            mImageView!!.setImageResource(mFrameRess!![pFrameNo])

            if (pFrameNo == mLastFrameNo)
                viewLoading?.visibility = View.VISIBLE
                //stop
                //playConstant(0)
            else
                playConstant(pFrameNo + 1)
        },mDuration.toLong())
        //pFrameNo == mLastFrameNo && mBreakDelay > 0) mBreakDelay else mDuration.toLong()
    }

}