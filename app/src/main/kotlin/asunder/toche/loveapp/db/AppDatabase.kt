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
        val CREATE_PROVINCE =" CREATE TABLE "+ TABLE_PROVINCE+
                "("+
                KEY_PROV_ID + " TEXT," +
                KEY_PROV_TH + " TEXT," +
                KEY_PROV_ENG + " TEXT," +
                KEY_PROV_LOCX + " TEXT," +
                KEY_PROV_LOCY + " TExT"+
                ")"
        db.execSQL(CREATE_PROVINCE)
        val CREATE_RISK_QUESTION =" CREATE TABLE "+ TABLE_RISK_QUESTION+
                "("+
                KEY_CHOICE_ID + " TEXT," +
                KEY_TITLE_TH + " TEXT," +
                KEY_TITLE_ENG + " TEXT," +
                KEY_QUES1_TH + " TEXT," +
                KEY_QUES1_ENG + " TEXT," +
                KEY_QUES2_TH + " TEXT," +
                KEY_QUES2_ENG + " TEXT," +
                KEY_QUES3_TH + " TEXT," +
                KEY_QUES3_ENG + " TEXT," +
                KEY_QUES4_TH + " TEXT," +
                KEY_QUES4_ENG + " TEXT," +
                KEY_QUES5_TH + " TEXT," +
                KEY_QUES5_ENG + " TEXT," +
                KEY_QUES6_TH + " TEXT," +
                KEY_QUES6_ENG + " TEXT," +
                KEY_QUES7_TH + " TEXT," +
                KEY_QUES7_ENG + " TEXT" +
                ")"
        db.execSQL(CREATE_RISK_QUESTION)
        val CREATE_JOB =" CREATE TABLE "+ TABLE_JOB+
                "("+
                KEY_JOB_ID + " TEXT,"+
                KEY_JOB_TH + " TEXT,"+
                KEY_JOB_ENG + " TEXT"+
                ")"
        db.execSQL(CREATE_JOB)
        val CREATE_NATIONAL =" CREATE TABLE "+ TABLE_NATIONAL+
                "("+
                KEY_NATION_ID + " TEXT,"+
                KEY_NATION_TH + " TEXT,"+
                KEY_NATION_ENG + " TEXT"+
                ")"
        db.execSQL(CREATE_NATIONAL)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KNOWLEGDE_GROUP)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_KNOWLEGDE_CONTENT)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVINCE)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RISK_QUESTION)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NATIONAL)
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
    fun addKnowledgeContent(data:ObservableArrayList<Model.RepositoryKnowledge>){
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

    fun addProvince(data: ObservableArrayList<Model.Province>){
        // Create and/or open the database for writing
        val db = writableDatabase
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            for(content in data){
                val values = ContentValues()
                values.put(KEY_PROV_ID, content.province_id)
                values.put(KEY_PROV_TH,content.province_th)
                values.put(KEY_PROV_ENG,content.province_eng)
                values.put(KEY_PROV_LOCX,content.locx.toString())
                values.put(KEY_PROV_LOCY,content.locy.toString())
                db.insertOrThrow(TABLE_PROVINCE, null, values)
                d{"Insert ["+content.province_id+"] ["+content.province_th+"] ["+content.province_eng+"]"}
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getProvince() : ObservableArrayList<Model.Province> {
        var contentList = ObservableArrayList<Model.Province>()
        d{"getKnowledgeContent"}
        // Select All Query
        val selectQuery = "SELECT * FROM $TABLE_PROVINCE"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    contentList.apply {
                        add(Model.Province(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3).toDouble(),
                                cursor.getString(4).toDouble())
                            )
                    }
                } while (cursor.moveToNext())
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
    fun deleteAllProvince(){
        val db = writableDatabase
        d{"Delete all record province"}
        db.delete(TABLE_PROVINCE,null,null)
        db.close()
    }
    fun addRiskQustion(data:ObservableArrayList<Model.RiskQuestion>){
        // Create and/or open the database for writing
        val db = writableDatabase
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            for(content in data){
                val values = ContentValues()
                values.put(KEY_CHOICE_ID, content.choice_id)
                values.put(KEY_TITLE_TH,content.title_th)
                values.put(KEY_TITLE_ENG,content.title_eng)
                values.put(KEY_QUES1_TH,content.question1_th)
                values.put(KEY_QUES1_ENG,content.question1_eng)
                values.put(KEY_QUES2_TH,content.question2_th)
                values.put(KEY_QUES2_ENG,content.question2_eng)
                values.put(KEY_QUES3_TH,content.question3_th)
                values.put(KEY_QUES3_ENG,content.question3_eng)
                values.put(KEY_QUES4_TH,content.question4_th)
                values.put(KEY_QUES4_ENG,content.question4_eng)
                values.put(KEY_QUES5_TH,content.question5_th)
                values.put(KEY_QUES5_ENG,content.question5_eng)
                values.put(KEY_QUES6_TH,content.question6_th)
                values.put(KEY_QUES6_ENG,content.question6_eng)
                values.put(KEY_QUES7_TH,content.question7_th)
                values.put(KEY_QUES7_ENG,content.question7_eng)
                db.insertOrThrow(TABLE_RISK_QUESTION, null, values)
                d{"Insert ["+content.choice_id+"] ["+content.title_eng+"] ["+content.title_th+"]"}
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }

    }
    fun getRiskQuestion()  : ObservableArrayList<Model.RiskQuestion> {
        var contentList = ObservableArrayList<Model.RiskQuestion>()
        d{"getRiskQuestion"}
        // Select All Query
        val selectQuery = "SELECT * FROM $TABLE_RISK_QUESTION"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    contentList.apply {
                        add(Model.RiskQuestion(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(7),
                                cursor.getString(8),
                                cursor.getString(9),
                                cursor.getString(10),
                                cursor.getString(11),
                                cursor.getString(12),
                                cursor.getString(13),
                                cursor.getString(14),
                                cursor.getString(15),
                                cursor.getString(16))
                        )
                    }
                } while (cursor.moveToNext())
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
    fun deleteAllRiskQuestion(){
        val db = writableDatabase
        d{"Delete all record risk question"}
        db.delete(TABLE_RISK_QUESTION,null,null)
        db.close()
    }

    //db job
    fun addJobs(data:ObservableArrayList<Model.RepositoryJob>){
        // Create and/or open the database for writing
        val db = writableDatabase
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            for(content in data){
                val values = ContentValues()
                values.put(KEY_JOB_ID, content.occupation_id)
                values.put(KEY_JOB_TH,content.occupation_th)
                values.put(KEY_JOB_ENG,content.occupation_eng)
                db.insertOrThrow(TABLE_JOB, null, values)
                d{"Insert ["+content.occupation_id+"] ["+content.occupation_eng+"] ["+content.occupation_th+"]"}
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }

    }
    fun getJobs()  : ObservableArrayList<Model.RepositoryJob> {
        var contentList = ObservableArrayList<Model.RepositoryJob>()
        d{"getJobs"}
        // Select All Query
        val selectQuery = "SELECT * FROM $TABLE_JOB"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    contentList.apply {
                        add(Model.RepositoryJob(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2))
                        )
                    }
                } while (cursor.moveToNext())
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

    //db national
    fun addNationals(data:ObservableArrayList<Model.RepositoryNational>){
        // Create and/or open the database for writing
        val db = writableDatabase
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction()
        try {
            for(content in data){
                val values = ContentValues()
                values.put(KEY_NATION_ID, content.national_id)
                values.put(KEY_NATION_TH,content.nationality_th)
                values.put(KEY_NATION_ENG,content.nationality_eng)
                db.insertOrThrow(TABLE_NATIONAL, null, values)
                d{"Insert ["+content.national_id+"] ["+content.nationality_eng+"] ["+content.nationality_th+"]"}
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            d{"Error while trying to add login to database"}
        } finally {
            db.endTransaction()
            db.close()
        }

    }
    fun getNations()  : ObservableArrayList<Model.RepositoryNational> {
        var contentList = ObservableArrayList<Model.RepositoryNational>()
        d{"getJobs"}
        // Select All Query
        val selectQuery = "SELECT * FROM $TABLE_NATIONAL"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery,null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    contentList.apply {
                        add(Model.RepositoryNational(
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2))
                        )
                    }
                } while (cursor.moveToNext())
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

    fun deleteAllContentWhenStart(){
        val db = writableDatabase
        d{"Delete all record risk question"}
        db.delete(TABLE_RISK_QUESTION,null,null)
        db.delete(TABLE_KNOWLEGDE_CONTENT,null,null)
        db.delete(TABLE_KNOWLEGDE_GROUP,null,null)
        db.delete(TABLE_PROVINCE,null,null)
        db.delete(TABLE_JOB,null,null)
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
        private val TABLE_PROVINCE="province"
        private val TABLE_RISK_QUESTION="risk_question"
        private val TABLE_JOB ="job"
        private val TABLE_NATIONAL="national"

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

        //province
        private val KEY_PROV_ID ="province_id"
        private val KEY_PROV_TH ="province_th"
        private val KEY_PROV_ENG = "province_eng"
        private val KEY_PROV_LOCX ="locx"
        private val KEY_PROV_LOCY ="locy"
        //risk question
        private val KEY_CHOICE_ID="choice_id"
        private val KEY_TITLE_TH="title_th"
        private val KEY_TITLE_ENG="title_eng"
        private val KEY_QUES1_TH="question1_th"
        private val KEY_QUES1_ENG="question1_eng"
        private val KEY_QUES2_TH="question2_th"
        private val KEY_QUES2_ENG="question2_eng"
        private val KEY_QUES3_TH="question3_th"
        private val KEY_QUES3_ENG="question3_eng"
        private val KEY_QUES4_TH="question4_th"
        private val KEY_QUES4_ENG="question4_eng"
        private val KEY_QUES5_TH="question5_th"
        private val KEY_QUES5_ENG="question5_eng"
        private val KEY_QUES6_TH="question6_th"
        private val KEY_QUES6_ENG="question6_eng"
        private val KEY_QUES7_TH="question7_th"
        private val KEY_QUES7_ENG="question7_eng"
        //job
        private val KEY_JOB_ID ="occupation_id"
        private val KEY_JOB_TH ="occupation_th"
        private val KEY_JOB_ENG="occupation_eng"
        //national
        private val KEY_NATION_ID="national_id"
        private val KEY_NATION_TH="national_th"
        private val KEY_NATION_ENG="national_eng"

    }
}