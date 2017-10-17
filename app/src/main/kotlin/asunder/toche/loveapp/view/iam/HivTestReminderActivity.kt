package asunder.toche.loveapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import asunder.toche.loveapp.databinding.PillItemBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by ToCHe on 10/11/2017 AD.
 */
class HivTestReminderActivity : AppCompatActivity(){


    var service : LoveAppService = LoveAppService.create()

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

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hiv_test_list)

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