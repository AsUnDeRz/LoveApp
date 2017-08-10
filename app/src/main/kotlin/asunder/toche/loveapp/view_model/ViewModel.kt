package asunder.toche.loveapp

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.github.ajalt.timberkt.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by admin on 8/7/2017 AD.
 */
object ViewModel{

    class HomeViewModel : ViewModel() {
        lateinit var service : LoveAppService

        init {
            service = LoveAppService.create()
        }

        interface HomeInterface { fun endCallProgress(data: ObservableList<Model.ImageHome>) }

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

    }




}