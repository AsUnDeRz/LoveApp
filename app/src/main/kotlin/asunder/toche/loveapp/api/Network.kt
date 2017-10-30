package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by admin on 8/9/2017 AD.
 */
interface LoveAppService{

    @GET("api/hiv_test/user_id/{id}")
    fun getHivTest(@Path("id") userId: String) : Observable<ArrayList<Model.RepositoryHivTest>>

    @GET("api/nationals/")
    fun getNationals() : Observable<ArrayList<Model.RepositoryNational>>

    @GET("api/occupations")
    fun getJobs() : Observable<ArrayList<Model.RepositoryJob>>

    @GET("api/games")
    fun getGames() : Observable<ArrayList<Model.RepositoryGame>>

    @GET("api/lab_result/user_id/{id}")
    fun getLabResult(@Path("id") userID: String) :Observable<ArrayList<Model.RepositoryLabResult>>

    @FormUrlEncoded
    @PUT("api/pill_tracking/")
    fun addTracking(@Field("date") date:String,@Field("user_id") userID: String,@Field("status") status: String) : Call<Void>

    @FormUrlEncoded
    @PUT("api/feedback/")
    fun addFeedback(@Field("user_id") userID: String,@Field("datetime") date:Date,@Field("comment") comment:String,@Field("rate") rate:String): Call<Void>

    @FormUrlEncoded
    @POST("api/user2")
    fun updateUser(@Field("user_id") userID: String,@Field("gender_id") genderID:String,@Field("name") name:String?,
                   @Field("first_name") fname:String?,@Field("first_surname") lname:String?,@Field("status_id") statusID:String?,
                   @Field("friend_id") fcode:String?,@Field("phone") phone:String?,@Field("email") email:String?,
                   @Field("password") password:String?,@Field("province") province:String?,@Field("job") job:String?,
                   @Field("iden_id") idCard:String?,@Field("birth") birthDay:String?,@Field("point") point:String?,@Field("national_id") nationaID:String?): Call<Void>

    @GET("api/knowledges/gender_id/{genderid}/group_id/{groupid}")
    fun getKnowledgeInGroup(@Path("genderid") gender_id: String,@Path("groupid") group_id:String) :Observable<ArrayList<Model.RepositoryKnowledge>>

    @GET("api/knowledge_groups/user_ids/{gender_id}/limit/5")
    fun getKnowledgeGroup(@Path("gender_id") gender_id:String) :Observable<ArrayList<Model.KnowledgeGroup>>


    @GET("api/question_for_games/game_id/{game_id}")
    fun getYesNoQuestionGame(@Path("game_id") game_id:String) : Observable<ArrayList<Model.QuestionYesNoGame>>

    @GET("api/yes_no_question/knowledge_id/{know_id}")
    fun getYesNoQuestion(@Path("know_id") know_id:String) : Observable<ArrayList<Model.QuestionYesNo>>

    @FormUrlEncoded
    @PUT("api/lab_result/")
    fun addLabResult(@Field("user_id") userID: String,@Field("viral") vl:String,@Field("cd4") cd4:String,@Field("test_date")testDate: Date): Call<Void>

    @FormUrlEncoded
    @PUT("api/hiv_test/")
    fun addHivTest(@Field("user_id") userID: String,@Field("test_date") testDate:Date) : Call<Void>

    @GET("api/user/user_id/{id}")
    fun getUser(@Path("id") userId: String) : Observable<ArrayList<Model.User>>

    @GET("api/user/email/{email_user}/password/{pass_user}")
    fun login(@Path("email_user") email:String,@Path("pass_user") password:String) :Observable<ArrayList<Model.User>>

    @GET("api/point_history/knowledge/user_id/{id}")
    fun getPointHistoryKnowledge(@Path("id") userId: String) : Observable<ArrayList<Model.RepoPointHistoryKnowledge>>

    @GET("api/point_history/game/user_id/{id}")
    fun getPointHistoryGame(@Path("id") userId: String) :Observable<ArrayList<Model.RepoPointHistoryGame>>

    @GET("api/point_history/risk_meter/user_id/{id}")
    fun getPointHistoryRiskMeter(@Path("id") userId: String) :Observable<ArrayList<Model.RepoPointHistoryRiskMeter>>

    //pointTypeID 2=knowledge 1=game Question 3= resultRiskmeter
    @FormUrlEncoded
    @PUT("api/point_history")
    fun addPointHistory(@Field("point_history_id") referID:String,@Field("user_id") userID:String,
                        @Field("point_type_id") pointTypeId:String,@Field("point") point:String,
                        @Field("date") date:Date) : Call<Void>

    @FormUrlEncoded
    @POST("api/user/point")
    fun addUserPoint(@Field("user_id")userId: String,@Field("point") point:String): Call<Void>

    @FormUrlEncoded
    @PUT("api/risk_test")
    fun addRiskTest(@Field("user_id") userId:String,@Field("risk_id") riskResult:String,
                    @Field("test_date") date:Date)  : Call<Void>

    @GET("api/provinces")
    fun getProvinces() : Observable<ArrayList<Model.Province>>


    @GET("api/hospitals/locx/{lat}/locy/{long}/distance/{dis}")
    fun getHospitalsOnMap(@Path("lat") lat:String,@Path("long") lng:String,@Path("dis")distance:String) :
            Observable<ArrayList<Model.RepositoryHospital>>

    @GET("api/knowledges/user_id/{userid}/limit/5")
    fun getContentInHome(@Path("userid") userId:String) : Observable<ArrayList<Model.RepositoryKnowledge>>

    @GET("api/notifications")
    fun getNotiMessage() :Observable<ArrayList<Model.RepositoryNotiMsn>>

    @FormUrlEncoded
    @POST("api/user/")
    fun updateHivStatus(@Field("status_id") status:String,@Field("user_id") userId:String) : Call<Void>

    @FormUrlEncoded
    @PUT("api/user2/")
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

        fun create() : LoveAppService {
            val gsonBuilder = GsonBuilder()

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(true)
                    .build()


            val restAdapter = Retrofit.Builder()
                    .baseUrl(BuildConfig.ENDPOINT)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build()

            return restAdapter.create(LoveAppService::class.java)
        }
    }

}

interface newService{


    /*
    API
Register new user โดยส่ง gender_id ไป (POST)
http://loveapponline.com/api/user


เป็นการ Update user หลังจากทำข้อ 1 (POST, ส่งให้ครบเหมือนของเดิมเลยครับ)
http://loveapponline.com/api/user2
ตรง email/password ให้เข้ารหัส base64 มาด้วยนะคับ


หลักจาก register เสร็จ แล้วจะทำการ Login (GET)
http://loveapponline.com/api/user/email/c3VwYXBhazk5OUBnbWFpbC5jb20=/password/MTIzNDU2
ตรงหลัง email กับ หลัง password ให้เข้ารหัส base64 มาด้วยนะคับ
     */
    @FormUrlEncoded
    @POST("api/user")
    fun Register(@Field("gender_id") genderID: String) : Observable<Model.RepoUser>

    @FormUrlEncoded
    @POST("api/user2")
    fun UpdateUser(@Field("user_id") userID: String,@Field("gender_id") genderID:String,@Field("name") name:String?,
                @Field("first_name") fname:String?,@Field("first_surname") lname:String?,@Field("status_id") statusID:String?,
                @Field("friend_id") fcode:String?,@Field("phone") phone:String?,@Field("email") email:String?,
                @Field("password") password:String?,@Field("province") province:String?,@Field("job") job:String?,
                @Field("iden_id") idCard:String?,@Field("birth") birthDay:String?,@Field("point") point:String?,@Field("national_id") nationaID:String?): Observable<Model.RepoResponse>

    @GET("api/user/email/{email_user}/password/{pass_user}")
    fun GetUser(@Path("email_user") email:String,@Path("pass_user") password: String) : Observable<Model.RepoUser>

    companion object {

        fun create() : newService {
            val gsonBuilder = GsonBuilder()

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY


            val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(true)
                    .build()



            val restAdapter = Retrofit.Builder()
                    .baseUrl(BuildConfig.NEWENDPOINT)
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build()

            return restAdapter.create(newService::class.java)
        }
    }

}