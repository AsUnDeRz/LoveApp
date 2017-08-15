package asunder.toche.loveapp

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by admin on 8/9/2017 AD.
 */
interface LoveAppService{

    @GET("api/images")
    fun getImageHome() : Observable<ArrayList<Model.ImageHome>>

    //@POST("mobile/main")
    //fun getProductGroup(): Observable<Model.ProductGroupResponse>

    //@FormUrlEncoded
    //@POST("mobile/product")
    //fun getProductByGroup(@Field("group_id") id:String) : Observable<Model.ProductResponse>

    companion object {
        private val BASE_URL = "http://loveapp1.herokuapp.com/"

        fun create() : LoveAppService {
            val gsonBuilder = GsonBuilder()

            //val loggingInterceptor = HttpLoggingInterceptor()
            //loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            //val client: OkHttpClient = OkHttpClient()
            //client.interceptors().add_blue(loggingInterceptor)

            val restAdapter = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build()

            return restAdapter.create(LoveAppService::class.java)
        }
    }

}