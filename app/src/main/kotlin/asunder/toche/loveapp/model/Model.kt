package asunder.toche.loveapp

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by admin on 8/7/2017 AD.
 */
object Model{

    data class Province(val province_id:String,val province_th: String,val province_eng:String,val locx:Double,val locy:Double)
    data class RepositoryHospital(val id: String, val name_th: String, val name_eng: String, val address_th: String,
                                  val address_eng: String, val service_th: String, val service_eng: String, var logo: String,
                                  var icon: String, val open_hour_th: String, val phone: String, val email: String, val province: String,
                                  val locx: Double, val locy: Double, val version: String, var isbooking: String, var isBook: String,
                                  val open_hour_eng: String, val hospital_id: String, val file_name: String, val file_path: String)


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
    data class HomeContent(var id:Long,var name:String,var point:String,var data:RepositoryContentHome): StableId{
        override val stableId: Long = id

    }

    data class Game2(val itemGame:ObservableArrayList<Model.Game1>,val randomPick:ObservableArrayList<Game1>)
    data class Game1(val id:Int, val icon:Int, var position:Int,var iconFront:Int)

    data class Clinic(var id: Long,var name:String,var testName:String,var workTime:String,var icon:Int):StableId{
        override val stableId: Long = id
    }


    data class UserId(var user_id:String)
    data class HivStatus(var status_id:String,var status_eng:String, var status_th:String)
    data class RepositoryGender(var gender_id:Long,var gender_type_th:String,var gender_type_eng:String)
    data class Gender(var id:Long,var title:String):StableId{
        override val stableId : Long = id
    }
    data class LearnNewContent(var id:Long,var title:String,var point:String):StableId{
        override val stableId : Long = id
    }
    data class LearnGameContent(var id:Int,var title:String,var desc:String,var percent:String):StableId{
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


    data class RepoPointHistoryRiskMeter(val risk_status: String,val date: Date,val point: String)
    data class RepoPointHistoryGame(val game_name_th:String,val game_name_eng:String,val date:Date,val point:String)
    data class RepoPointHistoryKnowledge(val title_th:String,val title_eng: String,val date:Date,val point:String)

    data class User(val user_id:String,val gender:String,val name:String,val first_name:String,val first_surename:String,
                    val status_id: String,val friend_id:String,val phone:String,val email:String,val password:String,
                    val province:String,val job:String,val iden_id:String,val birth:String,val point:String,
                    val password_hash:String,val remember_token:String,val updated_at:String)

    data class RepositoryContentHome(val id: String, val group_id: String, val title_th: String, val title_eng: String,
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
            val CREATOR: Parcelable.Creator<RepositoryContentHome> = object : Parcelable.Creator<RepositoryContentHome> {
                override fun createFromParcel(source: Parcel): RepositoryContentHome = RepositoryContentHome(source)
                override fun newArray(size: Int): Array<RepositoryContentHome?> = arrayOfNulls(size)
            }
        }
    }


}