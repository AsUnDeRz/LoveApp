package asunder.toche.loveapp

import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.databinding.PillItemBinding
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.hiv_test_list.*
import java.util.*


/**
 * Created by ToCHe on 10/11/2017 AD.
 */
class HivTestReminderActivity : AppCompatActivity(){


    var service : LoveAppService = LoveAppService.create()
    lateinit var prefer :SharedPreferences

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

    fun loadHivTest(){
        manageSub(
                service.getHivTest(prefer.getString(KEYPREFER.UserId,""))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data = ObservableArrayList<Model.PillTracking>().apply {
                                c.forEach {
                                    item ->
                                    if(item.test_date.time >= Date().time) {
                                        add(Model.PillTracking(item.user_id.toLong(),item.test_date.time,"HIV Test"))
                                    }
                                    d { "add [" + item.test_date + "] to array" }
                                }
                            }
                            rv_hiv_test.adapter = HivTestAdapter(data.sortedByDescending { it.date },false)
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hiv_test_list)
        prefer = PreferenceManager.getDefaultSharedPreferences(this@HivTestReminderActivity)
        loadHivTest()

        Glide.with(this@HivTestReminderActivity)
                .load(R.drawable.bg_white)
                .into(bg_root)

        rv_hiv_test.layoutManager = LinearLayoutManager(this)
        rv_hiv_test.setHasFixedSize(true)


    }

    fun HivTestAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.pillItem,stableIds).type{ item, position ->
            when(item){
                is Model.PillTracking -> hivTestType
                else -> null
            }
        }
    }

    private val hivTestType = Type<PillItemBinding>(R.layout.pill_item)
            .onCreate { println("Created ${it.binding.pillItem} at #${it.adapterPosition}") }
            .onClick {}



    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}