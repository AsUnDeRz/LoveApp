package asunder.toche.loveapp

import android.content.Context
import android.databinding.ObservableArrayList
import android.graphics.Typeface
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList


/**
 * Created by admin on 8/7/2017 AD.
 */
class Utils(private var context: Context) {

    val heavy: Typeface
        get() = Typeface.createFromAsset(context.assets, "fonts/AvenirLTStd-Heavy.otf")

    val medium: Typeface
        get() = Typeface.createFromAsset(context.assets, "fonts/AvenirLTStd-Medium.otf")


    fun getDataGame1Chapter1():ObservableArrayList<Model.Game1>{
        var idRandom = getRandom(1)
        val data1= ObservableArrayList<Model.Game1>().apply{
            add(Model.Game1(1, Data.iconGame1[idRandom[0]], 1))
            add(Model.Game1(1, Data.iconGame1[idRandom[0]], 1))
        }

        return data1
    }

    fun getDataGame1Chapter2():ObservableArrayList<Model.Game1>{
        //2 picture 4 model
        var idRandom = getRandom(2)
        val data2= ObservableArrayList<Model.Game1>().apply{
            for(i in 0..1){
                add(Model.Game1(i, Data.iconGame1[idRandom[i]], 1))
                add(Model.Game1(i, Data.iconGame1[idRandom[i]], 1))
            }
        }
        shuffleArray(data2)
        return data2
    }

    fun getDataGame1Chapter3():ObservableArrayList<Model.Game1>{
        //8 picture 16 model
        var idRandom = getRandom(8)
        val data3= ObservableArrayList<Model.Game1>().apply{
            for(i in 0..7) {
                add(Model.Game1(i, Data.iconGame1[idRandom[i]], 1))
                add(Model.Game1(i, Data.iconGame1[idRandom[i]], 1))
            }
        }
        shuffleArray(data3)
        return data3
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


    fun getRandom(dataSize:Int): ArrayList<Int>{
        var length = 69
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