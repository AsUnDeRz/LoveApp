package asunder.toche.loveapp

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteOpenHelper
import com.github.ajalt.timberkt.Timber.d
import android.databinding.adapters.TextViewBindingAdapter.setPhoneNumber
import java.util.*
import kotlin.collections.ArrayList


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
                KEY_NOTI_TIME + " DATE,"+
                KEY_NOTI_STATUS + " INTEGER"+
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



    fun addNotification(data:Model.Notification){
        // Create and/or open the database for writing
        val db = writableDatabase

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            val values = ContentValues()
            values.put(KEY_NOTI_TITLE, data.title)
            values.put(KEY_NOTI_DESC, data.message)
            values.put(KEY_NOTI_TIME,data.time.time)
            values.put(KEY_NOTI_STATUS,KEYPREFER.WAITING)
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_NOTIFICATION, null, values)
            db.setTransactionSuccessful()
            d{"Insert ["+data.title+"] ["+data.message+"] ["+data.time.toString()+"][Status["+KEYPREFER.WAITING+"]]"}
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }

    }
    fun updateNotification(data:Model.Notification,status:Int){
        val db = writableDatabase
        val values = ContentValues()
        values.put(KEY_NOTI_TITLE, data.title)
        values.put(KEY_NOTI_DESC, data.message)
        values.put(KEY_NOTI_TIME,data.time.time)
        values.put(KEY_NOTI_STATUS,status)

        d{"update notification with id ["+data.id+"] status [$status]"}
        // updating row
        db.update(TABLE_NOTIFICATION, values, KEY_NOTI_ID + " = ?", arrayOf(data.id))
        db.close()

    }
    fun deleteNotification(id:String){
        val db = writableDatabase
        d{ "Delete notification with id[$id]" }
        db.delete(TABLE_NOTIFICATION, KEY_NOTI_ID + " = ?", arrayOf(id))
        db.close()
    }

    fun deleteAllNoti(){
        val db = writableDatabase
        d{"Delete all record notification"}
        db.delete(TABLE_NOTIFICATION,null,null)
        db.close()

    }

    fun getNotificationList() : ArrayList<Model.Notification>{
        val notiList = ArrayList<Model.Notification>()
        // Select All Query
        val selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION

        val db = writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        try{
            if (cursor.moveToFirst()) {
                do {
                    notiList.apply {
                        add(Model.Notification(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                Date(cursor.getLong(3))
                        ))
                    }
                } while (cursor.moveToNext())
            }

        } catch (e: Exception) {
        d{"Error while trying to get posts from database"}
        } finally {
        if (cursor != null && !cursor.isClosed) {
            cursor.close()
        }
        db.close()
        }
        // return contact list
        return notiList
    }


    fun getCountNoti(): Model.NotiPercent{
        val tracked = getNotiTracked().size.toString()
        val missing = getNotiMissing().size.toString()
        val waiting = getNotiWaiting().size.toString()
        val totalNoti = getNotificationList().size.toString()

        d{"tracked [$tracked] Missing [$missing] Waiting [$waiting] Total [$totalNoti]"}
        return Model.NotiPercent(tracked,missing,waiting,totalNoti)
    }


    fun getNotiWaiting():ArrayList<Model.Notification>{
        d{"Select with Status Waiting"}
        return getNotisWithStatus(KEYPREFER.WAITING)
    }
    fun getNotiMissing():ArrayList<Model.Notification>{
        d{"Select with Status Missing"}
        return getNotisWithStatus(KEYPREFER.MISSING)
    }
    fun getNotiTracked():ArrayList<Model.Notification>{
        d{"Select with Status Tracked"}
        return getNotisWithStatus(KEYPREFER.TRACKED)

    }
    fun getNotisWithStatus(status:Int) : ArrayList<Model.Notification> {
        val notiList = ArrayList<Model.Notification>()
        // Select All Query
        val selectQuery = "SELECT  * FROM $TABLE_NOTIFICATION WHERE $KEY_NOTI_STATUS =?"

        val db = writableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(status.toString()))

        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    notiList.apply {
                        add(Model.Notification(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                Date(cursor.getLong(3))
                        ))
                    }
                } while (cursor.moveToNext())
            }

        } catch (e: Exception) {
            d { "Error while trying to get posts from database" }
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            db.close()
        }

        // return contact list
        return notiList
    }

    fun getNotiWithState(id:String) : Model.Notification{
        var notification = Model.Notification("123","123","123", Date())
        val db = readableDatabase
        val cursor = db.query(TABLE_NOTIFICATION, arrayOf(KEY_NOTI_ID, KEY_NOTI_TITLE, KEY_NOTI_DESC, KEY_NOTI_TIME),
                KEY_NOTI_ID +"=?", arrayOf(id),null,null,null,null)
        try{
            cursor?.moveToFirst()
            notification = Model.Notification(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                    Date(cursor.getLong(3)))
        }catch (e: Exception) {
            d{"Error while trying to get posts from database"}
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            db.close()
        }

        return notification
    }

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
        private val KEY_NOTI_STATUS = "status"
        //status notification
        //waiting  missing tracked


    }
}