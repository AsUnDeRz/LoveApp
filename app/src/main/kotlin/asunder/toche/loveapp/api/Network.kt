package asunder.toche.loveapp

import android.databinding.ObservableList
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

/**
 * Created by admin on 8/9/2017 AD.
 */
interface LoveAppService{


    @FormUrlEncoded
    @POST("api/user/point")
    fun addUserPoint(@Field("user_id")userId: String,@Field("point") point:String): Call<Void>

    @FormUrlEncoded
    @PUT("api/risk_test")
    fun addRiskTest(@Field("user_id") userId:String,@Field("risk_id") riskResult:String,
                    @Field("test_date") date:Date)  : Call<Void>

    @GET("api/provinces")
    fun getProvinces() : Observable<ArrayList<Model.Province>>


    @GET("api/hospitals/locx/{long}/locy/{lat}/distance/{dis}")
    fun getHospitalsOnMap(@Path("long") lng:String,@Path("lat") lat:String,@Path("dis")distance:String) :
            Observable<ArrayList<Model.RepositoryHospital>>

    @GET("api/knowledges/user_id/{userid}/limit/5")
    fun getContentInHome(@Path("userid") userId:String) : Observable<ArrayList<Model.RepositoryContentHome>>

    @GET("api/notifications")
    fun getNotiMessage() :Observable<ArrayList<Model.RepositoryNotiMsn>>

    @FormUrlEncoded
    @POST("api/user/")
    fun updateHivStatus(@Field("status_id") status:String,@Field("user_id") userId:String) : Call<Void>

    @FormUrlEncoded
    @PUT("api/user/")
    fun genUserID(@Field("gender_id") genderId:String) :Observable<ArrayList<Model.UserId>>

    @GET("api/hiv_status")
    fun getHivStatus() :Observable<ArrayList<Model.HivStatus>>

    @GET("api/genders")
    fun getGenders() : Observable<ArrayList<Model.RepositoryGender>>

    @GET("api/images")
    fun getImageHome() : Observable<ArrayList<Model.ImageHome>>

    @GET("api/risk_questions")
    fun getRiskQuestions(): Observable<ArrayList<Model.RiskQuestion>>

    //http://loveapp1.herokuapp.com/api/risk_meters/risk_id/2112142
    @GET("api/risk_meters/risk_id/{result}")
    fun getRiskResults(@Path("result") riskAnswer:String) : Observable<ArrayList<Model.RiskResult>>



    //@POST("mobile/main")
    //fun getProductGroup(): Observable<Model.ProductGroupResponse>

    //@FormUrlEncoded
    //@POST("mobile/product")
    //fun getProductByGroup(@Field("group_id") id:String) : Observable<Model.ProductResponse>

    companion object {
        private val BASE_URL = "http://loveapp1.herokuapp.com/"

        fun create() : LoveAppService {
            val gsonBuilder = GsonBuilder()

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()


            val restAdapter = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build()

            return restAdapter.create(LoveAppService::class.java)
        }
    }

}