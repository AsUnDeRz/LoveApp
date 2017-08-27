package asunder.toche.loveapp

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.view.animation.AccelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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
        interface RiskQInterface { fun endCallProgress(data: ObservableList<Model.RiskQuestion>) }

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

        fun loadContentHome(callback :HomeInterface,user_id:String){
            manageSub(
                    service.getContentInHome(user_id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ c -> run {
                                val data = ObservableArrayList<Model.RepositoryContentHome>().apply {
                                    c.forEach {
                                        item -> add(item)
                                        d{"add contentId ["+item.id+"]"}
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




}