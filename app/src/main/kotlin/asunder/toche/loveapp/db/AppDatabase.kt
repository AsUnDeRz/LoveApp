package asunder.toche.loveapp

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteOpenHelper
import android.databinding.ObservableArrayList
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
        val CREATE_KNOWLEDGE_GROUP_TABLE ="CREATE TABLE "+ TABLE_KNOWLEGDE_GROUP +
                "("+
                KEY_KG_ID + " TEXT," +
                KEY_KG_NAME_TH +" TEXT," +
                KEY_KG_NAME_EN +" TEXT," +
                KEY_KG_VERSION +" TEXT," +
                KEY_KG_POINT +" TEXT" +
                ")"
        db.execSQL(CREATE_KNOWLEDGE_GROUP_TABLE)
        val CREATE_KNOWLEDGE_CONTENT =" CREATE TABLE "+ TABLE_KNOWLEGDE_CONTENT+
                "("+
                KEY_KC_ID + " TEXT," +
                KEY_KC_GROUP_ID + " TEXT," +
                KEY_KC_TITLE_TH + " TEXT," +
                KEY_KC_TITLE_EN + " TEXT," +
                KEY_KC_CONTENT_TH + " TEXT," +
                KEY_KC_CONTENT_EN + " TEXT," +
                KEY_KC_CONTENT_LONG_TH + " TEXT," +
                KEY_KC_CONTENT_LONG_EN + " TEXT," +
                KEY_KC_IMAGE + " TEXT," +
                KEY_KC_VERSION + " TEXT," +
                KEY_KC_POINT + " TEXT," +
                KEY_KC_LINK + " TEXT" +
                ")"
        db.execSQL(CREATE_KNOWLEDGE_CONTENT)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KNOWLEGDE_GROUP)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KNOWLEGDE_CONTENT)
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

    fun getNotiWithState(id:String) : Model.Notification?{
        var notification :Model.Notification? = null
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

    //function knowledge group
    fun addKnowledgeGroup(data:ArrayList<Model.KnowledgeGroup>){
        // Create and/or open the database for writing
        val db = writableDatabase
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            for(group in data){
                val values = ContentValues()
                values.put(KEY_KG_ID, group.group_id)
                values.put(KEY_KG_NAME_TH, group.group_name_th)
                values.put(KEY_KG_NAME_EN,group.group_name_eng)
                values.put(KEY_KG_VERSION,group.version)
                values.put(KEY_KG_POINT,group.sumpoint)
                db.insertOrThrow(TABLE_KNOWLEGDE_GROUP, null, values)
                d{"Insert ["+group.group_id+"] ["+group.group_name_eng+"] ["+group.sumpoint+"]"}
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getKnowledgeGroup() :ObservableArrayList<Model.KnowledgeGroup>{
        val knowledgeGroupList = ObservableArrayList<Model.KnowledgeGroup>()
        // Select All Query
        val selectQuery = "SELECT  * FROM $TABLE_KNOWLEGDE_GROUP"

        val db = writableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    knowledgeGroupList.apply {
                        add(Model.KnowledgeGroup(
                                cursor.getString(0),
                                cursor.getString(2),
                                cursor.getString(1),
                                cursor.getString(3),
                                cursor.getString(4)))
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
        // return  list
        return knowledgeGroupList
    }

    fun deleteAllKnowledgeGroup(){
        val db = writableDatabase
        d{"Delete all record knowledge group"}
        db.delete(TABLE_KNOWLEGDE_GROUP,null,null)
        db.close()
    }


    //function knowledge content
    fun addKnowledgeContent(data:ArrayList<Model.RepositoryKnowledge>){
        // Create and/or open the database for writing
        val db = writableDatabase
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            for(content in data){
                val values = ContentValues()
                var contentTHlong=""
                var contentENlong=""
                var link=""
                if(content.content_th_long != null){
                    contentTHlong = content.content_th_long
                }
                if(content.content_eng_long != null){
                    contentENlong = content.content_eng_long
                }
                if(content.link != null){
                    link = content.link
                }
                values.put(KEY_KC_ID, content.id)
                values.put(KEY_KC_GROUP_ID,content.group_id)
                values.put(KEY_KC_TITLE_TH,content.title_th)
                values.put(KEY_KC_TITLE_EN,content.title_eng)
                values.put(KEY_KC_CONTENT_TH,content.content_th)
                values.put(KEY_KC_CONTENT_EN,content.content_eng)
                values.put(KEY_KC_CONTENT_LONG_TH,contentTHlong)
                values.put(KEY_KC_CONTENT_LONG_EN,contentENlong)
                values.put(KEY_KC_IMAGE,"") // test content must null only
                values.put(KEY_KC_VERSION,content.version)
                values.put(KEY_KC_POINT,content.point)
                values.put(KEY_KC_LINK,link)
                db.insertOrThrow(TABLE_KNOWLEGDE_CONTENT, null, values)
                d{"Insert ["+content.id+"] ["+content.title_eng+"] ["+content.point+"]"}
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getKnowledgeContent() : ObservableArrayList<Model.RepositoryKnowledge> {
        var contentList = ObservableArrayList<Model.RepositoryKnowledge>()
        d{"getKnowledgeContent"}
        // Select All Query
        val selectQuery = "SELECT * FROM $TABLE_KNOWLEGDE_CONTENT"

        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    contentList.apply {
                        add(Model.RepositoryKnowledge(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(9),
                                cursor.getString(10),
                                arrayListOf("1","2","3","4","5"),
                                cursor.getString(9),
                                cursor.getString(6),
                                cursor.getString(7),
                                cursor.getString(11)
                            )
                        )
                    }
                } while (cursor.moveToNext())
            }
            if(contentList.size > 0) {
                val master = ObservableArrayList<Model.RepositoryKnowledge>().apply {
                    for (i in 0..2) {
                        add(contentList[i])
                    }
                }
                contentList = master
            }

        } catch (e: Exception) {
            d { "Error while trying to get posts from database ["+e.message+"]" }
        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
            db.close()
        }
        // return  list

        return contentList
    }

    fun getKnowledgeContent(id:String) :ObservableArrayList<Model.RepositoryKnowledge>{
        val contentList = ObservableArrayList<Model.RepositoryKnowledge>()
        d{"getKnowledgeContent by $id"}
        // Select All Query
        val selectQuery = "SELECT  * FROM $TABLE_KNOWLEGDE_CONTENT WHERE $KEY_KC_GROUP_ID =? "

        val db = writableDatabase
        val cursor = db.rawQuery(selectQuery,arrayOf(id))
        try {
            if (cursor.moveToFirst()) {
                do {
                    contentList.apply {
                        add(Model.RepositoryKnowledge(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(9),
                                cursor.getString(10),
                                arrayListOf("1","2","3","4","5"),
                                cursor.getString(9),
                                cursor.getString(6),
                                cursor.getString(7),
                                cursor.getString(11)
                        )
                        )
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
        // return  list
        return contentList
    }

    fun deleteAllKnowledgeContent(){
        val db = writableDatabase
        d{"Delete all record knowledge content"}
        db.delete(TABLE_KNOWLEGDE_CONTENT,null,null)
        db.close()
    }
    companion object {
        // Database Info
        private val DATABASE_NAME = "loveapp"
        private val DATABASE_VERSION = 1

        // Table Names
        private val TABLE_USER = "user"
        private val TABLE_NOTIFICATION ="notification"
        private val TABLE_KNOWLEGDE_GROUP="knowlegde_group"
        private val TABLE_KNOWLEGDE_CONTENT="knowlegde_content"

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

        //knowledge group
        private val KEY_KG_ID ="id"
        private val KEY_KG_NAME_TH ="group_name_th"
        private val KEY_KG_NAME_EN ="group_name_eng"
        private val KEY_KG_VERSION ="version"
        private val KEY_KG_POINT ="point"

        //knowledge content
        private val KEY_KC_ID ="id"
        private val KEY_KC_GROUP_ID = "group_id"
        private val KEY_KC_TITLE_TH ="title_th"
        private val KEY_KC_TITLE_EN ="title_eng"
        private val KEY_KC_CONTENT_TH ="content_th"
        private val KEY_KC_CONTENT_EN ="content_eng"
        private val KEY_KC_IMAGE ="image_byte"
        private val KEY_KC_VERSION ="version"
        private val KEY_KC_POINT ="point"
        private val KEY_KC_CONTENT_LONG_TH ="content_long_th"
        private val KEY_KC_CONTENT_LONG_EN ="content_long_eng"
        private val KEY_KC_LINK ="link"



    }
}