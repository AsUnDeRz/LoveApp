package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.LearnNewItemBinding
import kotlinx.android.synthetic.main.header_logo_blue.*
import kotlinx.android.synthetic.main.learnandgame.*


/**
 * Created by admin on 8/7/2017 AD.
 */
class LearnAndGameFragment : Fragment() {

    val learnList = ObservableArrayList<Model.LearnNewContent>().apply {
        add(Model.LearnNewContent(23,"title","20"))
        add(Model.LearnNewContent(23,"title","20"))
        add(Model.LearnNewContent(23,"title","20"))

    }

    companion object {
        fun newInstance(): LearnAndGameFragment {
            return LearnAndGameFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.learnandgame, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title_app.text = "LEARNS\nAND GAMES"


        rv_learn_game.layoutManager = LinearLayoutManager(context)
        rv_learn_game.setHasFixedSize(true)
        rv_learn_game.adapter = LearnNewAdapter(learnList,false)

        btn_game.setOnClickListener {
            val data = Intent()
            data.putExtra("title","GAMES")
            data.putExtra("key",1)
            startActivity(data.setClass(context, LearnGameMainActivity::class.java))
        }
        btn_learn.setOnClickListener {
            val data = Intent()
            //data.putExtra("title","LEARNS")
            //data.putExtra("key",2)
            startActivity(data.setClass(context, LearnTopicActivity::class.java))
        }

    }

    private val LearnNewType = Type<LearnNewItemBinding>(R.layout.learn_new_item)
            .onCreate { println("Created ${it.binding.learnNewItem} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.learnNewItem} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.learnNewItem} at #${it.adapterPosition}") }
            .onClick {
                val data = Intent()
                startActivity(data.setClass(context, LearnNewsActivity::class.java))

            }
            .onLongClick {}
    fun LearnNewAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.learnNewItem,stableIds).type{ item, position ->
            when(item){
                is Model.LearnNewContent -> LearnNewType
                else -> null
            }
        }
    }
}