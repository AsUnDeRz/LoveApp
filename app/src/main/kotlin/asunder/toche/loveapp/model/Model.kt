package asunder.toche.loveapp

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.sql.Timestamp
import java.util.*

/**
 * Created by admin on 8/7/2017 AD.
 */
object Model{

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
    data class HomeContent(var id:Long,var name:String): StableId{
        override val stableId: Long = id

    }

    data class Game2(val itemGame:ObservableArrayList<Model.Game1>,val randomPick:ObservableArrayList<Game1>)
    data class Game1(val id:Int, val icon:Int, var position:Int,var iconFront:Int)

    data class Clinic(var id: Long,var name:String,var testName:String,var workTime:String,var icon:Int):StableId{
        override val stableId: Long = id
    }

    data class Gender(var id:Long,var gender:String):StableId{
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

    data class Notification(var id:Long,var message:String,var title:String,var time:Date):StableId{
        override val stableId : Long = id
    }

    data class PillReminder(var id:Long,var message: String,var time: Date):StableId{
        override val stableId : Long = id
    }

    data class Point(var id:Long,var title:String,var points:String):StableId{
        override val stableId : Long = id
    }

    data class NotiMessage(var id:Long,var title:String):StableId{
        override val stableId :Long = id
    }

}