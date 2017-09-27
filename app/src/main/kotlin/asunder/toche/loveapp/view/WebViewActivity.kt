package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebSettings
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.webview.*


/**
 * Created by ToCHe on 9/25/2017 AD.
 */
class WebViewActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)


        val url = intent.getStringExtra("KEY_URL")
        webView.webViewClient = WebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.loadUrl(url)
    }
}