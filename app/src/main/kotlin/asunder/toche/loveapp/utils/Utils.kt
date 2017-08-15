package asunder.toche.loveapp

import android.content.Context
import android.databinding.ObservableArrayList
import android.graphics.Typeface
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by admin on 8/7/2017 AD.
 */
class Utils(private var context: Context) {

    val heavy: Typeface
        get() = Typeface.createFromAsset(context.assets, "fonts/AvenirLTStd-Heavy.otf")

    val medium: Typeface
        get() = Typeface.createFromAsset(context.assets, "fonts/AvenirLTStd-Medium.otf")

    fun px2dp(f: Float): Int {
        val metrics = context.resources.displayMetrics
        val dp = f
        val fpixels = metrics.density * dp
        return (fpixels + 0.5f).toInt()
    }

    fun getDataGame1(duo:Int):ObservableArrayList<Model.Game1>{
        var duoSize = duo
        duoSize--
        var idRandom = getRandom(69,duo)
        val data1= ObservableArrayList<Model.Game1>().apply{
            for(i in 0..duoSize){
                add(Model.Game1(i, DataSimple.iconGame1[idRandom[i]], 1))
                add(Model.Game1(i, DataSimple.iconGame1[idRandom[i]], 1))
            }
        }
        shuffleArray(data1)
        return data1
    }


    fun getDataGame2():Model.Game2{
        var idRandom = getRandom(79,30)
        val dataGame2 = ObservableArrayList<Model.Game1>().apply {
            for(i in 0..29){
                add(Model.Game1(i, DataSimple.iconGame2[idRandom[i]], 1))
            }
        }
        shuffleArray(dataGame2)
        shuffleList(idRandom)
        val randomPick = ObservableArrayList<Model.Game1>().apply {
            for(i in 0..29){
                add(Model.Game1(i, DataSimple.iconGame2[idRandom[i]], 1))
            }
        }

        return Model.Game2(dataGame2,randomPick)
    }

    fun shuffleArray(data: ObservableArrayList<Model.Game1>) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        val rnd = Random()
        for (i in data.size - 1 downTo 1) {
            val index = rnd.nextInt(i + 1)
            // Simple swap
            val a = data[index]
            data[index] = data[i]
            data[i] = a
        }
    }

    fun shuffleList(data: ArrayList<Int>){
        // If running on Java 6 or older, use `new Random()` on RHS here
        val rnd = Random()
        for (i in data.size - 1 downTo 1) {
            val index = rnd.nextInt(i + 1)
            // Simple swap
            val a = data[index]
            data[index] = data[i]
            data[i] = a
        }
    }




    fun getRandom(range:Int,dataSize:Int): ArrayList<Int>{
        var length = range
        var size = length- dataSize
        val numbers = ArrayList<Int>()
        while (length > size) {
            val digit = (Math.random() *length).toInt()
            if (numbers.contains(digit)) continue
            numbers.add(digit)
            length--
        }
        //test
        val sb = StringBuilder()
        for (integer in numbers) {
            sb.append("/"+integer)
        }
        return numbers
    }

}