package asunder.toche.loveapp

import android.content.Intent
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import asunder.toche.loveapp.R
import asunder.toche.loveapp.databinding.LearnTopicItemBinding
import kotlinx.android.synthetic.main.header_logo_white.*
import kotlinx.android.synthetic.main.learn_topic.*

/**
 * Created by admin on 8/14/2017 AD.
 */
class LearnTopicActivity : AppCompatActivity() {

        val data = ObservableArrayList<Model.LearnTopicContent>().apply {
            add(Model.LearnTopicContent(1,"How to hack a save sex1","1642 Points","13 Topics",R.drawable.clinic_img))
            add(Model.LearnTopicContent(2,"How to hack a save sex2","1113 Points","20 Topics",R.drawable.clinic_img))
            add(Model.LearnTopicContent(3,"How to hack a save sex3","15 Points","3 Topics",R.drawable.clinic_img))

        }


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.learn_topic)

            title_app.text = "LEARNS"

            rv_learn_topic.setHasFixedSize(true)
            rv_learn_topic.layoutManager = LinearLayoutManager(this)
            rv_learn_topic.adapter =LearnTopicAdapter(data,false)


        }



    private val LearnTopicType = Type<LearnTopicItemBinding>(R.layout.learn_topic_item)
            .onCreate { println("Created ${it.binding.topic} at #${it.adapterPosition}") }
            .onBind { println("Bound ${it.binding.topic} at #${it.adapterPosition}") }
            .onRecycle { println("Recycled ${it.binding.topic} at #${it.adapterPosition}") }
            .onClick {
                val data = Intent()
                data.putExtra("title","LEARNS")
                data.putExtra("key",2)
                startActivity(data.setClass(this, LearnGameMainActivity::class.java))
            }
            .onLongClick {}


    fun LearnTopicAdapter(item: List<Any>, stableIds: Boolean): LastAdapter {
        return LastAdapter(item,BR.topic,stableIds).type{ item, position ->
            when(item){
                is Model.LearnTopicContent -> LearnTopicType
                else -> null
            }
        }
    }


}
