package asunder.toche.loveapp

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.preference.PreferenceManager
import android.view.animation.AccelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by admin on 8/7/2017 AD.
 */
object ViewModel{

    class Game2ViewModel(val context: Context,val model: Model.Game1){
        object GameBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: String) {
                Glide.with(view.context).load(url).into(view)
            }
        }
    }
    class Game1ViewModel(val context: Context,val model:Model.Game1) {
        interface  updateData{
            fun updateList(positionClick:Int)
        }
        object GameBindingAdapter {
            @BindingAdapter("bind:imageUrl")
            @JvmStatic
            fun loadImage(view: ImageView, url: String) {
                Glide.with(view.context).load(url).into(view)
            }
        }

        fun onClickImage(p1:ImageButton,p2:ImageButton,isFirst:Boolean) {
            if (isFirst) {
                actionItem(0f,90f,p1,p2,isFirst)
            } else {
                actionItem(0f,-90f,p1,p2,isFirst)

            }
        }
        fun actionItem(from:Float,to:Float,p1:ImageButton,p2:ImageButton,isFirst: Boolean) {
            // Find the center of image
            val centerX = p1.width / 2.0f
            val centerY = p1.height / 2.0f

            // Create a new 3D rotation with the supplied parameter
            // The animation listener is used to trigger the next animation
            val rotation = Flip3d(from, to, centerX, centerY)
            rotation.duration = 100
            rotation.fillAfter = true
            rotation.interpolator = AccelerateInterpolator()
            rotation.setAnimationListener(DisplayNextView(isFirst,p1,p2))
            if(isFirst) {
                p1.startAnimation(rotation)
            }else{
                p2.startAnimation(rotation)
            }
        }


    }


    class MainViewModel :ViewModel(){
        var service : LoveAppService = LoveAppService.create()
        interface RiskQInterface { fun endCallProgress(any: Any) }

        private var _compoSub = CompositeDisposable()
        private val compoSub: CompositeDisposable
            get() {
                if (_compoSub.isDisposed) {
                    _compoSub = CompositeDisposable()
                }
                return _compoSub
            }

        protected final fun manageSub(s: Disposable) = compoSub.add(s)

        fun unsubscribe() { compoSub.dispose() }

        fun loadRiskQuestion(callback:RiskQInterface){
            manageSub(
                    service.getRiskQuestions()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                val data = ObservableArrayList<Model.RiskQuestion>().apply {
                                    c.forEach {
                                        item -> add(item)
                                        Timber.d { "Test api Risk quesiton show title eng[" + item.title_eng + "]" }
                                    }
                                }
                                /*
                                val riskQuestion1 = data[6]
                                data.removeAt(6)
                                val rawData = ObservableArrayList<Model.RiskQuestion>().apply {
                                    add(riskQuestion1)
                                    data.forEach {
                                        item -> add(item)
                                    }
                                }
                                */
                                callback.endCallProgress(data)
                                d { "check response [" + c.size + "]" }
                            }},{
                                d { it.message!! }
                            })
            )
        }

        fun loadJob(callback: RiskQInterface){
            manageSub(
                    service.getJobs()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c ->
                                run {
                                    val data = ObservableArrayList<Model.RepositoryJob>().apply {
                                        c.forEach { item ->
                                            add(item)
                                            d { "add job [" + item.occupation_th + "]" }
                                        }
                                    }
                                    callback.endCallProgress(data)
                                    d { "check response [" + c.size + "]" }
                                }
                            }, {
                                d {"on Error "+ it.message }
                            })
            )
        }
        fun loadProvince(callback:RiskQInterface){
            manageSub(
                    service.getProvinces()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                val data =ObservableArrayList<Model.Province>().apply {
                                    c.forEach {
                                        item -> run {
                                        add(item)
                                        }}}

                                callback.endCallProgress(data)
                                d { "check response [" + c.size + "]" }

                            }},{
                                d { it.message!! }
                            })
            )
        }
        fun loadNational(callback:RiskQInterface){
            manageSub(
                    service.getNationals()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                val data =ObservableArrayList<Model.RepositoryNational>().apply {
                                    c.forEach {
                                        item -> run {
                                        add(item)
                                    }}}

                                callback.endCallProgress(data)
                                d { "check response [" + c.size + "]" }

                            }},{
                                d { it.message!! }
                            })
            )
        }

        fun loadKnowledage(callback: RiskQInterface,user_id: String){
            manageSub(
                    service.getContentInHome(user_id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c ->
                                run {
                                    val data = ObservableArrayList<Model.RepositoryKnowledge>().apply {
                                        c.forEach { item ->
                                            add(item)
                                            d { "add contentId [" + item.id + "]" }
                                        }
                                    }
                                    callback.endCallProgress(data)
                                    d { "check response [" + c.size + "]" }
                                }
                            }, {
                                d {"on Error "+ it.message }
                            })
            )
        }

        fun loadKnowledgeGroup(genderID:String,callback: RiskQInterface,utils: Utils){
            manageSub(
                    service.getKnowledgeGroup(genderID)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                val data = ObservableArrayList<Model.KnowledgeGroup>().apply {
                                    c.forEach {
                                        item -> add(item)
                                        d { "add [" + item.group_name_eng + "] to array" }
                                    }
                                }
                                callback.endCallProgress(data)
                                d { "check response [" + c.size + "]" }
                            }},{
                                d { it.message!! }
                            })
            )
        }

    }

    class HomeViewModel : ViewModel() {
        lateinit var service : LoveAppService

        init {
            service = LoveAppService.create()
        }

        interface HomeInterface { fun endCallProgress(any:Any) }

        private var _compoSub = CompositeDisposable()
        private val compoSub: CompositeDisposable
            get() {
                if (_compoSub.isDisposed) {
                    _compoSub = CompositeDisposable()
                }
                return _compoSub
            }

        protected final fun manageSub(s: Disposable) = compoSub.add(s)

        fun unsubscribe() { compoSub.dispose() }

        fun loadImage(callback: HomeInterface){
            manageSub(
                    service.getImageHome()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                val data = ObservableArrayList<Model.ImageHome>().apply {
                                    c.forEach {
                                        item -> add(item)
                                        d{item.link}
                                    }
                                }
                                callback.endCallProgress(data)
                                d{"check response ["+c.size+"]"}
                            }},{
                                d{ it.message!! }
                            })
            )
        }

        fun loadContentInLocal(callback: HomeInterface,appDatabase: AppDatabase){
            async(UI){
                val data : Deferred<ArrayList<Model.RepositoryKnowledge>> = bg {
                    appDatabase.getKnowledgeContent()
                }
                callback.endCallProgress(data.await())
            }
        }

        fun checkDataUser(data:Model.User) : Boolean{
            //if(data.name == null){ return false }
            if(data.first_name == null){ return false }
            if(data.first_surname == null){ return false}
            if(data.friend_id == null){ return false}
            if(data.phone == null){ return false}
            if(data.email == null){ return false}
            if(data.password == null){ return false}
            if(data.province == null){ return false}
            if(data.job == null){ return false}
            //if(data.iden_id == null){ return false}
            if(data.birth == null){ return false}
            return true
        }

        fun loadUser(id:String,callback: HomeInterface,context: Context,items:ObservableArrayList<Model.RepositoryKnowledge>){
            manageSub(
                    service.getUser(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                val data = ObservableArrayList<Model.User>().apply {
                                    c.forEach {
                                        item -> add(item)
                                        d { "Add [$item] to arraylist" }
                                    }
                                }
                                d { "check response [" + c.size + "]" }
                                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                                val editor = preferences.edit()
                                if(!checkDataUser(data[0])){
                                    d{"return User No update Data"}
                                    editor.putBoolean(KEYPREFER.isUpdateProfile,false)
                                    editor.apply()
                                }else{
                                    d{"return User Updated Data"}
                                    editor.putBoolean(KEYPREFER.isUpdateProfile,true)
                                    editor.apply()
                                }
                                callback.endCallProgress(items)

                            }},{
                                d { it.message!! }
                            })
            )
        }
        fun loadContentHome(callback :HomeInterface,user_id:String,appDatabase: AppDatabase,context: Context) {
            if (appDatabase.getKnowledgeContent().size != 0) {
                d{"load content in local"}
                val data = appDatabase.getKnowledgeContent()
                loadUser(user_id,callback,context,data)
            }else{
                manageSub(
                        service.getContentInHome(user_id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ c ->
                                    run {
                                        val data = ObservableArrayList<Model.RepositoryKnowledge>().apply {
                                            c.forEach { item ->
                                                add(item)
                                                d { "add contentId [" + item.id + "]" }
                                            }
                                        }
                                        appDatabase.addKnowledgeContent(data)
                                        loadUser(user_id,callback,context,data)
                                        //callback.endCallProgress(appDatabase.getKnowledgeContent())
                                        d { "check response [" + c.size + "]" }
                                    }
                                }, {
                                    d {"on Error "+ it.message }
                                    //load with local
                                    d{"load content on server"}
                                    if (appDatabase.getKnowledgeContent().size != 0) {
                                        d{"load content in local"}
                                        val data = appDatabase.getKnowledgeContent()
                                        loadUser(user_id,callback,context,data)
                                        //callback.endCallProgress(data)
                                    }
                                })
                )
            }
        }

        fun addUpdatePoint(point:String,contentId:String,userId: String){
                val addPoint = service.addUserPoint(userId,point)
                addPoint.enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Timber.d { t?.message.toString() }
                    }
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if(response!!.isSuccessful){
                            Timber.d { "addPoint Successful" }
                            inputPointHistory(point,contentId,userId)
                        }
                    }
                })
        }
        fun addUpdatePoint(point:String,userId: String){
            val addPoint = service.addUserPoint(userId,point)
            addPoint.enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    Timber.d { t?.message.toString() }
                }
                override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                    if(response!!.isSuccessful){
                        Timber.d { "addPoint Successful" }
                    }
                }
            })
        }
        fun inputPointHistory(point:String,knowledgeID:String,id:String){
            Timber.d { "input point history" }
                Timber.d { "point [$point] user_id[$id]" }
                val addPH = service.addPointHistory(knowledgeID,id,"2",point, Date())
                addPH.enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>?, t: Throwable?) {
                        Timber.d { t?.message.toString() }
                    }
                    override fun onResponse(call: Call<Void>?, response: Response<Void>?) {
                        if (response!!.isSuccessful) {
                            Timber.d { "addPointHistory successful" }
                        }
                    }
                })

        }

    }


    class StepNewUser : ViewModel() {

        lateinit var service : LoveAppService

        init {
            service = LoveAppService.create()
        }

        interface StepInterface { fun endCallProgress(any:Any) }

        private var _compoSub = CompositeDisposable()
        private val compoSub: CompositeDisposable
            get() {
                if (_compoSub.isDisposed) {
                    _compoSub = CompositeDisposable()
                }
                return _compoSub
            }

        protected final fun manageSub(s: Disposable) = compoSub.add(s)

        fun unsubscribe() { compoSub.dispose() }

        fun loadGender(){

        }

        fun getUserId(){

        }

        fun loadKnowleadge(){

        }

        fun updateUser(){

        }


    }




}