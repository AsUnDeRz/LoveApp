package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import com.github.ajalt.timberkt.Timber.d
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


/**
 * Created by ToCHe on 10/28/2017 AD.
 */
class Debug:AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.debug_view)


        val email ="rerom2536@gmail.com"
        val pass ="12345"

        val enEmail= Base64.encodeToString(email.toByteArray(),Base64.DEFAULT)
        val enPass= Base64.encodeToString(pass.toByteArray(),Base64.DEFAULT)
        d{"email encode = $enEmail"}
        d{"pass encode =$enPass"}

    }
}