package asunder.toche.loveapp

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by admin on 8/7/2017 AD.
 */
object Model{

    /*
     "header": {
        "code": 200,
        "msg": {
     */
    data class RepoUser(var header:RepoUserDetail)
    data class RepoUserDetail(var code:String,var msg:Any)
    data class RepoResponse(var header:RepoDetail)
    data class RepoDetail(var msg:String, var code: String)

    data class RepositoryHivTest(val user_id:String,val test_date: Date)
    data class RepositoryNational(val national_id:String,val nationality_th:String,val nationality_eng:String)
    data class National(var id:Long,var title:String):StableId{
        override val stableId : Long = id
    }
    data class Language(var id:Long,var title:String,var key:String):StableId{
        override val stableId : Long = id
    }

    data class RepositoryJob(val occupation_id:String,val occupation_th: String,val occupation_eng: String)

    data class RepositoryGame(val game_id:String,val game_name_eng: String,val game_name_th: String,val sum_point:String)


    data class RepositoryLabResult(val user_id:String,val viral :String,val cd4:String,val test_date:Date)

    data class KnowledgeGroup(val group_id: String,val group_name_eng:String,val group_name_th:String,
                              val version:String,val sumpoint:String)

    data class QuestionYesNoGame(val question_id: String, val game_id: String, val question_th: String, val question_eng: String, val answer: Boolean, val point: String) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                1 == source.readInt(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(question_id)
            writeString(game_id)
            writeString(question_th)
            writeString(question_eng)
            writeInt((if (answer) 1 else 0))
            writeString(point)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<QuestionYesNoGame> = object : Parcelable.Creator<QuestionYesNoGame> {
                override fun createFromParcel(source: Parcel): QuestionYesNoGame = QuestionYesNoGame(source)
                override fun newArray(size: Int): Array<QuestionYesNoGame?> = arrayOfNulls(size)
            }
        }
    }

    data class QuestionYesNo(val question_id: String, val knowledge_id: String, val question_th: String, val question_eng: String, val answer: Boolean, val point: String) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                1 == source.readInt(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(question_id)
            writeString(knowledge_id)
            writeString(question_th)
            writeString(question_eng)
            writeInt((if (answer) 1 else 0))
            writeString(point)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<QuestionYesNo> = object : Parcelable.Creator<QuestionYesNo> {
                override fun createFromParcel(source: Parcel): QuestionYesNo = QuestionYesNo(source)
                override fun newArray(size: Int): Array<QuestionYesNo?> = arrayOfNulls(size)
            }
        }
    }

    data class Province(val province_id:String,val province_th: String,val province_eng:String,val locx:Double,val locy:Double)

    data class Clinic(var id: Long, var name: String, var address: String, var service: String, var open_hour: String, var phone: String,
                      var email: String, var province: String, var locx: Double, var locy: Double, var version: String, var isbooking: String,
                      var isBook: String, var hospital_id: String, var img_icon: String, var img_detail: String, var promo_id: String,
                      var promo_title: String, var promo_start: String, var promo_end: String) : StableId, Parcelable {
        override val stableId: Long = id

        object ImageViewBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: String) {
                Glide.with(view.context).load(url).into(view)
            }
        }

        constructor(source: Parcel) : this(
                source.readLong(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readDouble(),
                source.readDouble(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeLong(id)
            writeString(name)
            writeString(address)
            writeString(service)
            writeString(open_hour)
            writeString(phone)
            writeString(email)
            writeString(province)
            writeDouble(locx)
            writeDouble(locy)
            writeString(version)
            writeString(isbooking)
            writeString(isBook)
            writeString(hospital_id)
            writeString(img_icon)
            writeString(img_detail)
            writeString(promo_id)
            writeString(promo_title)
            writeString(promo_start)
            writeString(promo_end)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<Clinic> = object : Parcelable.Creator<Clinic> {
                override fun createFromParcel(source: Parcel): Clinic = Clinic(source)
                override fun newArray(size: Int): Array<Clinic?> = arrayOfNulls(size)
            }
        }
    }


    data class RepositoryHospital(val id: String, val name_th: String, val name_eng: String, val address_th: String,
                                  val address_eng: String, val service_th: String, val service_eng: String, var logo: String,
                                  var icon: String, val open_hour_th: String, val phone: String, val email: String, val province: String,
                                  val locx: Double, val locy: Double, val version: String, var isbooking: String, var isBook: String,
                                  val open_hour_eng: String, val hospital_id: String, val file_name: String, val file_path: String,
                                  var promotion_id:String,var promotion_th:String,var promotion_eng:String,var start_date:Date,var end_date:Date)


    data class RiskResult(val risk_id:String,val risk_status:String,val status:String,val details_th:String,val details_eng:String,
                          val topic_th:String,val topic_eng:String)
    data class RiskQuestion(val choice_id:String,val title_th:String,val title_eng:String,
                            val question1_th:String,val question1_eng:String,
                            val question2_th:String,val question2_eng:String,
                            val question3_th:String,val question3_eng:String,
                            val question4_th:String,val question4_eng:String,
                            val question5_th:String,val question5_eng:String,
                            val question6_th:String,val question6_eng:String,
                            val question7_th:String,val question7_eng:String)

    data class ImageHomeResponse(val list:MutableList<ImageHome>)
    data class ImageHome(val id:String,val image_byte:String,val version:String,val link:String)
    data class HomeContent(var id:Long,var name:String,var point:String,var data: RepositoryKnowledge): StableId{
        override val stableId: Long = id

    }

    data class Game2(val itemGame:ObservableArrayList<Model.Game1>,val randomPick:ObservableArrayList<Game1>)
    data class Game1(val id:Int, val icon:Int, var position:Int,var iconFront:Int)




    data class UserId(var user_id:String)
    data class HivStatus(var status_id:String,var status_eng:String, var status_th:String)
    data class RepositoryGender(var gender_id:Long,var gender_type_th:String,var gender_type_eng:String)
    data class Gender(var id:Long,var title:String):StableId{
        override val stableId : Long = id
    }
    data class LearnNewContent(var id:Long,var title:String,var point:String):StableId{
        override val stableId : Long = id
    }
    data class LearnGameContent(var id:Int,var title:String,var desc:String,var percent:String,var icon:Int):StableId{
        override val stableId : Long = id.toLong()

        object ImageViewBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: Int) {
                Glide.with(view.context).load(url).into(view)
            }
        }

    }

    data class LearnTopicContent(var id:Int,var title:String,var point:String,var size:String,var icon:Int):StableId{
        override val stableId : Long=id.toLong()

        object ImageViewBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: Int) {
                Glide.with(view.context).load(url).into(view)
            }
        }
    }

    data class Notification(var id:String,var title:String,var message:String,var time:Date):StableId{
        override val stableId : Long = id.toLong()
    }
    data class NotiPercent(val tracked:String,val missing:String,val waiting:String,val totalNoti:String)

    data class PillReminder(var id:Long,var message: String,var time: Date):StableId{
        override val stableId : Long = id

        fun getTimeShort():String{
            val formatter = SimpleDateFormat("hh:mm a")
            val dateString = formatter.format(time)
            return dateString
        }
    }

    data class Point(var time:Long,var title:String,var point:String):StableId{
        override val stableId : Long = time

        fun getTimeShort():String{
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val dateString = formatter.format(Date(time))
            return dateString
        }
    }

    data class RepositoryNotiMsn(var notification_id:String,var title_th:String,var title_eng:String)
    data class NotiMessage(var id:Long,var title:String):StableId{
        override val stableId :Long = id
    }
    data class PillTracking(var id:Long,var date:Long,var status:String) : StableId{
        override val stableId: Long =id

        fun getTimeShort():String{
            val formatter = SimpleDateFormat("dd/MM/yy HH:mm")
            val dateString = formatter.format(Date(date))
            return dateString
        }
        fun getTimeHiv():String{
            val formatter = SimpleDateFormat("dd/MM/yy")
            val dateString = formatter.format(Date(date))
            return dateString
        }
    }


    data class RepoPointHistoryRiskMeter(val risk_status: String,val date: Date,val point: String)
    data class RepoPointHistoryGame(val game_name_th:String,val game_name_eng:String,val date:Date,val point:String)
    data class RepoPointHistoryKnowledge(val title_th:String,val title_eng: String,val date:Date,val point:String)

    data class User(val user_id:String,val gender_id:String,val name:String?,val first_name:String?,val first_surname:String?,
                    val status_id: String?,val friend_id:String?,val phone:String?,val email:String?,val password:String?,
                    val province:String?,val job:String?,val iden_id:String?,val birth:String?,val point:String?,
                    val password_hash:String?,val remember_token:String?,val updated_at:String?,val national_id:String?)

    data class RepositoryKnowledge(val id: String, val group_id: String, val title_th: String, val title_eng: String,
                                   val content_th: String, val content_eng: String, val image_byte: String, val point: String,
                                   val gender_id: ArrayList<String>, val version: String, val content_th_long: String,
                                   val content_eng_long: String, val link: String) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.createStringArrayList(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(id)
            writeString(group_id)
            writeString(title_th)
            writeString(title_eng)
            writeString(content_th)
            writeString(content_eng)
            writeString(image_byte)
            writeString(point)
            writeStringList(gender_id)
            writeString(version)
            writeString(content_th_long)
            writeString(content_eng_long)
            writeString(link)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<RepositoryKnowledge> = object : Parcelable.Creator<RepositoryKnowledge> {
                override fun createFromParcel(source: Parcel): RepositoryKnowledge = RepositoryKnowledge(source)
                override fun newArray(size: Int): Array<RepositoryKnowledge?> = arrayOfNulls(size)
            }
        }
    }


}