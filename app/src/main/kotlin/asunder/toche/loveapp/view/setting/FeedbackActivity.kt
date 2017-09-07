package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.feedback.*
import kotlinx.android.synthetic.main.header_logo_white_back.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by admin on 8/13/2017 AD.
 */
class FeedbackActivity: AppCompatActivity(){


    var service : LoveAppService = LoveAppService.create()

    private var _compoSub = CompositeDisposable()
    private val compoSub: CompositeDisposable
        get() {
            if (_compoSub.isDisposed) {
                _compoSub = CompositeDisposable()
            }
            return _compoSub
        }

    protected final fun manageSub(s: Disposable) = compoSub.add(s)

    fun unsubscribe() { compoSub.dispose() }

    lateinit var utils :Utils


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback)
        utils = Utils(this@FeedbackActivity)

        Glide.with(this)
                .load(R.drawable.bg_feedback)
                .into(bg_feedback)
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)

        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_send.setOnClickListener {
            inputFeedback(editText.text.toString())
        }


    }

    fun inputFeedback(comment:String){
        btn_send.isClickable = false
        d { "input CD4 VL" }
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@FeedbackActivity)
        if (preferences.getString(KEYPREFER.UserId, "") != "") {
            val userID = preferences.getString(KEYPREFER.UserId,"")
            d { " user_id[" + preferences.getString(KEYPREFER.UserId, "") + "]" }
            val addCD4 = service.addFeedback(userID, Date(),comment)
            addCD4.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    d { t?.message.toString() }
                    btn_send.isClickable = true
                }

                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if (response!!.isSuccessful) {
                        d { "addFeedback successful" }
                        onBackPressed()
                    }
                    btn_send.isClickable = true
                }
            })
        }
    }
}