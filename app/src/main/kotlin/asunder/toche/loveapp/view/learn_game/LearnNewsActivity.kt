package asunder.toche.loveapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import asunder.toche.loveapp.R
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.learn_new.*
import kotlinx.android.synthetic.main.learn_new_finish.*
import android.widget.TextView




/**
 * Created by admin on 8/5/2017 AD.
 */
class LearnNewsActivity : AppCompatActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learn_new)
        val utils = Utils(this@LearnNewsActivity)

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
        var content = intent.getParcelableExtra<Model.RepositoryContentHome>(KEYPREFER.CONTENT)
        val data  = utils.txtLocale(content.content_th,content.content_eng)
        toolbar_layout.title = utils.txtLocale(content.title_th,content.title_eng)
        txt_f3.text ="Earned +"+content.point+" points."

        //val data  ="<p>One of the most effective tools to fight against sexually transmitted HIV is the male condom. Condoms are easy to use, cheap to buy and extremely efficient at preventing HIV transmission. Condoms come in all shapes, sizes, textures, tastes and materials. If you or a partner doesn&rsquo;t like one type, try another &ndash; the possibilities are endless!</p>\r\n\r\n<p><em>Remember: All condom packages should carry an expiry date. Throw them away if the date has passed. Try not to store condoms in direct sunlight or in very warm or cold places.</em></p>\r\n\r\n<p><em><strong>How to put a condom on:</strong></em></p>\r\n\r\n<ul>\r\n\t<li>Check expiry date.</li>\r\n\t<li>Open condom package (don&rsquo;t use your teeth, you might break the condom)</li>\r\n\t<li>Determine which way the condom is rolled.Tips up!\r\n\t<ul>\r\n\t\t<li>Placing on hand on either side, pinch the rolled ring of the condom between you thumb and finger</li>\r\n\t\t<li>Gently roll the condom in one direction with your fingers. If it resists rolling, this is not the direction in which you will &nbsp;want to unroll the condom over the penis</li>\r\n\t</ul>\r\n\t</li>\r\n\t<li>Get hard!</li>\r\n\t<li>Pinch the tip!</li>\r\n\t<li>Get rolling! The condom should easily unroll down the length of the shaft.</li>\r\n\t<li>Lube it up! Water- and silicon-based lubricants are both safe to use with latex, but water-based lube washes off more easily and won&rsquo;t stain your sheets.</li>\r\n\t<li>Sexy time!</li>\r\n</ul>\r\n\r\n<p><strong>How to take off a condom:</strong></p>\r\n\r\n<ul>\r\n\t<li>Immediately after ejaculation grasp the bottom of the condom with your hand and withdraw, preventing the condom from slipping off or spilling.</li>\r\n\t<li>Slide it off to remove it</li>\r\n\t<li>Tie a knot at the condom&rsquo;s opening to prevent the contents from spilling after its removal</li>\r\n\t<li>Throw the used condom in the trash</li>\r\n</ul>\r\n"
        val deleteN = data.replace("\n","")
        val deleteR = deleteN.replace("\r","")
        val deleteT = deleteR.replace("\t","")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            colunm_1.text = Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT)
        }else{
            colunm_1.text = Html.fromHtml(data)
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_readmore.setOnClickListener {
            val uri = Uri.parse("https://www.google.com")
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = uri
            startActivity(intent)
        }


    }
}