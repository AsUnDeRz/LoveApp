package asunder.toche.loveapp

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.sql.Timestamp
import java.util.*

/**
 * Created by admin on 8/7/2017 AD.
 */
object Model{

    data class HomeContent(var id:Long,var name:String): StableId{
        override val stableId: Long = id

    }

    data class Clinic(var id: Long,var name:String,var testName:String,var workTime:String):StableId{
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
    data class Notification(var id:Long,var message:String,var title:String,var time:Date):StableId{
        override val stableId : Long = id
    }

    data class PillReminder(var id:Long,var message: String,var time: Date):StableId{
        override val stableId : Long = id
    }

    data class Point(var id:Long,var title:String,var points:String):StableId{
        override val stableId : Long = id
    }

}