package asunder.toche.loveapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.LabeledIntent
import android.content.pm.PackageManager
import android.databinding.ObservableArrayList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.text.Html
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_new.*
import kotlinx.android.synthetic.main.learn_new_finish.*
import com.github.ajalt.timberkt.Timber.d
import android.text.Spanned
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import im.delight.android.webview.AdvancedWebView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnNewsActivity : AppCompatActivity() {

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

    var questionList = ObservableArrayList<Model.QuestionYesNo>()
    lateinit var content : Model.RepositoryKnowledge
    lateinit var utils :Utils
    lateinit var  handler: Handler
    lateinit var runnable: Runnable
    var readFinish = false

    override fun onBackPressed() {
        val data =Intent()
        if(readFinish) {
            data.putExtra(KEYPREFER.CONTENT, content.id)
            data.putExtra(KEYPREFER.POINT, content.point)
            setResult(Activity.RESULT_OK, data)
        }
        finish()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_new)
        val utils = Utils(this@LearnNewsActivity)

        handler = Handler()
        runnable = Runnable {
            final_layout.visibility = View.VISIBLE
        }


        /*
        (0 until toolbar.childCount)
                .map { toolbar.getChildAt(it) }
                .filterIsInstance<TextView>()
                .forEach {
                    it.typeface = MyApp.typeFace.heavy
                    it.setTextColor(ContextCompat.getColor(this@LearnNewsActivity, R.color.white))
                    it.text = "Learn"
                }
                */
        toolbar_layout.setExpandedTitleMargin(50,5,5,50)
        toolbar_layout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar)
        toolbar_layout.setCollapsedTitleTextColor(ContextCompat.getColor(this@LearnNewsActivity, R.color.white))
        toolbar_layout.setExpandedTitleColor(ContextCompat.getColor(this@LearnNewsActivity, R.color.white))
        toolbar_layout.setCollapsedTitleTypeface(MyApp.typeFace.heavy)
        toolbar_layout.setExpandedTitleTypeface(MyApp.typeFace.heavy)

        title_header.text = "LEARNS\nAND GAMES"
        content = intent.getParcelableExtra<Model.RepositoryKnowledge>(KEYPREFER.CONTENT)
        val data  = utils.txtLocale(content.content_th,content.content_eng)
        toolbar_layout.title = utils.txtLocale(content.title_th,content.title_eng)
        txt_f3.text ="Earned +"+content.point+" points."

        //val data  ="<p>One of the most effective tools to fight against sexually transmitted HIV is the male condom. Condoms are easy to use, cheap to buy and extremely efficient at preventing HIV transmission. Condoms come in all shapes, sizes, textures, tastes and materials. If you or a partner doesn&rsquo;t like one type, try another &ndash; the possibilities are endless!</p>\r\n\r\n<p><em>Remember: All condom packages should carry an expiry date. Throw them away if the date has passed. Try not to store condoms in direct sunlight or in very warm or cold places.</em></p>\r\n\r\n<p><em><strong>How to put a condom on:</strong></em></p>\r\n\r\n<ul>\r\n\t<li>Check expiry date.</li>\r\n\t<li>Open condom package (don&rsquo;t use your teeth, you might break the condom)</li>\r\n\t<li>Determine which way the condom is rolled.Tips up!\r\n\t<ul>\r\n\t\t<li>Placing on hand on either side, pinch the rolled ring of the condom between you thumb and finger</li>\r\n\t\t<li>Gently roll the condom in one direction with your fingers. If it resists rolling, this is not the direction in which you will &nbsp;want to unroll the condom over the penis</li>\r\n\t</ul>\r\n\t</li>\r\n\t<li>Get hard!</li>\r\n\t<li>Pinch the tip!</li>\r\n\t<li>Get rolling! The condom should easily unroll down the length of the shaft.</li>\r\n\t<li>Lube it up! Water- and silicon-based lubricants are both safe to use with latex, but water-based lube washes off more easily and won&rsquo;t stain your sheets.</li>\r\n\t<li>Sexy time!</li>\r\n</ul>\r\n\r\n<p><strong>How to take off a condom:</strong></p>\r\n\r\n<ul>\r\n\t<li>Immediately after ejaculation grasp the bottom of the condom with your hand and withdraw, preventing the condom from slipping off or spilling.</li>\r\n\t<li>Slide it off to remove it</li>\r\n\t<li>Tie a knot at the condom&rsquo;s opening to prevent the contents from spilling after its removal</li>\r\n\t<li>Throw the used condom in the trash</li>\r\n</ul>\r\n"
        val deleteN = data.replace("\n","")
        val deleteR = deleteN.replace("\r","")
        val deleteT = deleteR.replace("\t","")

        val htmlAsSpanned :Spanned

        d{ data }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            htmlAsSpanned = Html.fromHtml(data, Html.FROM_HTML_MODE_LEGACY)
        }else{
            htmlAsSpanned = Html.fromHtml(data)
        }
        val webview = findViewById<AdvancedWebView>(R.id.colunm_1)
        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webview.loadDataWithBaseURL("", data, "text/html", "UTF-8", "")
        btn_back.setOnClickListener {
            onBackPressed()
        }



        btn_readmore.setOnClickListener {
            val uri = Uri.parse(content.link.trim())
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = uri
            startActivity(intent)
        }

        btn_facebook.setOnClickListener {
                onShare(content.link)
        }

        btn_getmore.setOnClickListener {
            //load question
                val data = Intent()
                data.putExtra(KEYPREFER.CONTENT,content.id)
                startActivity(data.setClass(this@LearnNewsActivity, QuestionActivity::class.java))

        }


        scroller.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                //d{ "Scroll DOWN"}
            }
            if (scrollY < oldScrollY) {
                //d{ "Scroll UP"}
            }

            if (scrollY == 0) {
                //d{"TOP SCROLL"}
            }

            if (scrollY == (v!!.getChildAt(0).measuredHeight - v.measuredHeight)) {
                d{"BOTTOM SCROLL"}
                readFinish = true
            }

        }




    }

    fun onShare(link:String){

        val emailIntent = Intent()
        emailIntent.action = Intent.ACTION_SEND
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "test")
        emailIntent.type = "message/rfc822"

        val pm = packageManager
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"


        val openInChooser = Intent.createChooser(emailIntent, "Share")

        val resInfo = pm.queryIntentActivities(sendIntent, 0)
        val intentList = ArrayList<LabeledIntent>()
        for (ri in resInfo) {
            // Extract the label, append it, and repackage it in a LabeledIntent

            val packageName = ri.activityInfo.packageName
           if(packageName.contains("facebook")) {
                val intent = Intent()
                intent.component = ComponentName(packageName, ri.activityInfo.name)
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain";
                if(packageName.contains("facebook")) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, "data")
                    intent.putExtra(Intent.EXTRA_TEXT,"Title"+ link)
                    d{packageName+"   "+ri.activityInfo.processName}

                }
                intentList.add(LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon))
            }
        }

        // convert intentList to array
        val extraIntents = intentList.toArray(arrayOfNulls<LabeledIntent>(intentList.size))


        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents)
        startActivity(openInChooser)
    }


    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,2000)
        //loadYesNoQuestion()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        unsubscribe()
    }
}