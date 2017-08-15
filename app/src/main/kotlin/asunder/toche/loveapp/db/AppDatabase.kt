package asunder.toche.loveapp

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import model.Entities

/**
 * Created by admin on 8/15/2017 AD.
 */

@Database(entities = [(Entities.User::class), (Entities.Notification::class)],version =1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun userDao() : Entities.UserDao

    companion object {
        const val DATABASE_NAME = "LoveappDB"
    }
}