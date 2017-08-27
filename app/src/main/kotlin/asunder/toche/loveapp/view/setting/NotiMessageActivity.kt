package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.header_logo_white_back.*
import kotlinx.android.synthetic.main.noti_message.*
import android.text.InputType
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import android.app.Activity
import android.content.Intent
import asunder.toche.loveapp.databinding.NotiMessageItemBinding


/**
 * Created by admin on 8/13/2017 AD.
 */
class NotiMessageActivity:AppCompatActivity(){


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

    val notiMsnList = ObservableArrayList<Model.NotiMessage>()
    val utils = Utils(this@NotiMessageActivity)
    var message = ""


    fun loadNotiMessage(){
        manageSub(
                service.getNotiMessage()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            notiMsnList.apply {
                                c.forEach { (notification_id, title_th, title_eng) -> if(title_eng != null) add(Model.NotiMessage(notification_id.toLong(),utils.txtLocale(title_th, title_eng)))
                                    d { "Add [$title_th] to arraylist" }
                                }
                            }
                            rv_noti_msn.adapter = NotiMessageAdapter(notiMsnList,false)
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val returnIntent = Intent()
        returnIntent.putExtra(KEYPREFER.RESULT,message)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.noti_message)

        loadNotiMessage()
        Glide.with(this)
                .load(R.drawable.bg_white)
                .into(bg_root)
        btn_back.setOnClickListener {
            onBackPressed()
        }

        rv_noti_msn.layoutManager = LinearLayoutManager(this@NotiMessageActivity)
        rv_noti_msn.setHasFixedSize(true)



        btn_msn.setOnClickListener {
            showInputDialog()
        }


    }

    fun showInputDialog() {
        //.inputRange(2, 16)
        MaterialDialog.Builder(this)
                .typeface(utils.medium,utils.heavy)
                .title("Input")
                .content("New message")
                .inputType(
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME or InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .positiveText("SUBMIT")
                .input(
                        "Message",
                        "",
                        false,
                        { dialog, input -> notiMsnList.add(Model.NotiMessage(123,input.toString()))
                        rv_noti_msn.adapter = NotiMessageAdapter(notiMsnList,false)})
                .show()
    }


    fun NotiMessageAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.msnItem,stableIds).type{ item, position ->
            when(item){
                is Model.NotiMessage -> NotiMessageType
                else -> null
            }
        }
    }

    private val NotiMessageType = Type<NotiMessageItemBinding>(R.layout.noti_message_item)
            .onCreate { println("Created ${it.binding.msnItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.msnItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.msnItem} at #${it.adapterPosition}") }
            .onClick {
                message = it.binding.msnItem.title
                val returnIntent = Intent()
                returnIntent.putExtra(KEYPREFER.RESULT,message)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            .onLongClick {}


    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}