package model

import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * Created by admin on 8/15/2017 AD.
 */
object Entities{

    @Entity(tableName = "user")
    data class User(
        @PrimaryKey var id:Long?,
        var passcode:String,
        var gender:String?
    )

    @Entity(tableName = "notification")
    data class Notification(
            @PrimaryKey var id:Long?,
            var title:String,
            var time:String?
    )

    

    @Dao
    interface UserDao{

        @Query("SELECT * FROM user")
        fun loadUser():Flowable<User>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertUser(user:User):Unit
    }



}