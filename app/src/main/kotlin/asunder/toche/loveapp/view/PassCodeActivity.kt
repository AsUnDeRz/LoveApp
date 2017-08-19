package asunder.toche.loveapp

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import asunder.toche.loveapp.R
import com.bumptech.glide.Glide
import com.github.ajalt.timberkt.Timber.d
import kotlinx.android.synthetic.main.passcode.*

/**
 * Created by admin on 8/13/2017 AD.
 */
class PassCodeActivity: AppCompatActivity(){


    enum class StatePC{
        add,check
    }

    lateinit var statePasscode : StatePC
    var passcode = arrayOf("","","","")
    var iconId = arrayOf(R.drawable.no0,R.drawable.no1,R.drawable.no2,R.drawable.no3,R.drawable.no4,R.drawable.no5,
            R.drawable.no6,R.drawable.no7,R.drawable.no8,R.drawable.no9)
    var isChecked = false
    var numClick =0
    var pcInStateAdd =""
    var pcInStateCheck =""



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.passcode)

        Glide.with(this)
                .load(R.drawable.bg_blue_only)
                .into(bg_root)
        statePasscode = StatePC.add
        delete.setOnClickListener {
            deleteValues()
        }

        n0.setOnClickListener { addValues(0) }
        n1.setOnClickListener { addValues(1) }
        n2.setOnClickListener { addValues(2) }
        n3.setOnClickListener { addValues(3) }
        n4.setOnClickListener { addValues(4) }
        n5.setOnClickListener { addValues(5) }
        n6.setOnClickListener { addValues(6) }
        n7.setOnClickListener { addValues(7) }
        n8.setOnClickListener { addValues(8) }
        n9.setOnClickListener { addValues(9) }





    }



    fun deleteValues(){
        when {
            r4.isChecked -> {
                r4.isChecked = false
                passcode[3] =""
                numClick = 3
            }
            r3.isChecked -> {
                r3.isChecked = false
                passcode[2] =""
                numClick = 2
            }
            r2.isChecked -> {
                r2.isChecked = false
                passcode[1] =""
                numClick = 1
            }
            r1.isChecked -> {
                r1.isChecked = false
                passcode[0] =""
                numClick = 0
            }
        }
    }

    fun addValues(pc:Int){
        if(numClick < passcode.size) {
            passcode[numClick] = pc.toString()
            d{"Passcode add in position $numClick  values = $pc"}

            numClick++
            d{"Change numClick $numClick"}
            initValues()
        }else{
            numClick++
        }


    }

    fun initValues(){
        //init and update indicator
        for(i in 0..3){
            d{"for i in passcode i = $i"}
            if(!passcode[i].isEmpty()){
                d{"if else passcode != 0 is  values ="+passcode[i]}
                when(i){
                    0 ->r1.isChecked = true
                    1 ->r2.isChecked = true
                    2 ->r3.isChecked = true
                    3 ->r4.isChecked = true
                }
            }else{
                d{"if else passcode == 0 is  values ="+passcode[i]}
                when(i){
                    0 ->r1.isChecked = false
                    1 ->r2.isChecked = false
                    2 ->r3.isChecked = false
                    3 ->r4.isChecked = false
                }
            }
        }

        //update state
        if(r4.isChecked){
            //check state
            if(statePasscode == StatePC.add){
                statePasscode = StatePC.check
                var pc=""
                for(i in passcode)  pc+=i
                pcInStateAdd = pc
                Toast.makeText(this,pc,Toast.LENGTH_LONG).show()
                //clear state
                passcode = arrayOf("","","","")
                r1.isChecked = false
                r2.isChecked = false
                r3.isChecked = false
                r4.isChecked = false
                numClick =0
            }else{
                var pc=""
                for(i in passcode)  pc+=i
                if(pc == pcInStateAdd){
                    Toast.makeText(this,"Passcode Macthing Save in db",Toast.LENGTH_LONG).show()
                }

            }




        }

    }
}