package asunder.toche.loveapp

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteOpenHelper
import com.github.ajalt.timberkt.Timber.d


/**
 * Created by admin on 8/15/2017 AD.
 */

class AppDatabase(internal var myCon:Context) : SQLiteOpenHelper(myCon, DATABASE_NAME, null, DATABASE_VERSION) {


    internal var cursor: SQLiteCursor? = null
    internal lateinit var sqlDb: SQLiteDatabase

    fun open(): AppDatabase {

        sqlDb = this.writableDatabase
        return this
    }

    override fun close() {
        this.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER +
                "(" +
                KEY_LOGIN_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_LOGIN_USERID + " TEXT," + // Define a foreign key
                KEY_LOGIN_GENDERID + " TEXT" +
                ")"
        db.execSQL(CREATE_USER_TABLE)
        val CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION +
                "(" +
                KEY_NOTI_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_NOTI_TITLE + " TEXT," + // Define a foreign key
                KEY_NOTI_DESC + " TEXT," +
                KEY_NOTI_TIME + " DATE"+
                ")"
        db.execSQL(CREATE_NOTIFICATION_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER)
            onCreate(db)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION)
            onCreate(db)
        }
    }

    fun addUser(userid: String, genderid: String) {
        // Create and/or open the database for writing
        val db = writableDatabase

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            val values = ContentValues()
            values.put(KEY_LOGIN_USERID, userid)
            values.put(KEY_LOGIN_GENDERID, genderid)

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_USER, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun updateUser(){

    }
    fun deleteAllUser() {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_USER, null, null)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{ "Error while trying to delete all posts and users"}
        } finally {
            db.endTransaction()
            db.close()
        }
    }


    fun haveUser(): String {
        var mobile = "empty"
        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        val LOGIN_SELECT_QUERY = String.format("SELECT * FROM " + TABLE_USER)

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        val db = readableDatabase
        val cursor = db.rawQuery(LOGIN_SELECT_QUERY, null)
        try {
            d{"SQLLITE Count record in table login" +cursor!!.count}
            if (cursor!!.count === 1) {
                if (cursor!!.moveToFirst()) {
                    do {
                        mobile = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LOGIN_USERID))
                        d{"SQLLITE My Email = " + mobile}
                    } while (cursor.moveToNext())
                }
            }

        } catch (e: Exception) {
            d{"Error while trying to get posts from database"}
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            db.close()
        }
        return mobile
    }



    fun addNotification(){

    }
    fun updateNotification(){

    }
    fun deleteNotification(){

    }

    fun getNotificationList(){}

    companion object {
        // Database Info
        private val DATABASE_NAME = "loveapp"
        private val DATABASE_VERSION = 1

        // Table Names
        private val TABLE_USER = "user"
        private val TABLE_NOTIFICATION ="notification"

        // user Table Columns
        private val KEY_LOGIN_ID = "id"
        private val KEY_LOGIN_USERID = "userid"
        private val KEY_LOGIN_GENDERID = "genderid"

        //notification Table Columns
        private val KEY_NOTI_ID ="id"
        private val KEY_NOTI_TITLE ="title"
        private val KEY_NOTI_TIME = "time"
        private val KEY_NOTI_DESC ="desc"


    }
}