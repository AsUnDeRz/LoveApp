package asunder.toche.loveapp

import android.content.Intent
import android.content.SharedPreferences
import android.databinding.ObservableArrayList
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import asunder.toche.loveapp.R
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.cd4_vl.*
import kotlinx.android.synthetic.main.iam.*
import kotlinx.android.synthetic.main.lab_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import view.custom_view.TextViewHeavy
import java.util.*

/**
 * Created by admin on 8/1/2017 AD.
 */
class IamFragment : Fragment() {


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
    companion object {
        fun newInstance(): IamFragment {
            return IamFragment()
        }
    }

    lateinit var appDb :AppDatabase
    lateinit var mChart : LineChart
    lateinit var preference : SharedPreferences
    var dataUser = ObservableArrayList<Model.User>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.iam, container, false)
        appDb = AppDatabase(activity)
        preference = PreferenceManager.getDefaultSharedPreferences(activity)
        loadUser(preference.getString(KEYPREFER.UserId,"0"))

        return view
    }



    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //btn_viral_load.typeface = MyApp.typeFace.heavy
        //btn_cd4.typeface = MyApp.typeFace.heavy

        btn_point.setOnClickListener {
            context.startActivity(Intent().setClass(context,PointHistriesActivity::class.java))
        }
        btn_riskmeter.setOnClickListener {
            context.startActivity(Intent().setClass(context,RiskMeterActivity::class.java))

        }
        labresult.setOnInflateListener { viewStub, view ->
            initGraph(view)
        }
        iamnonhiv.setOnInflateListener { viewStub, view ->
            d{"i am non hiv inflate"}
            val btnLastTest = view.findViewById<LinearLayout>(R.id.btn_last_test)
            val btnNextTest = view.findViewById<LinearLayout>(R.id.btn_next_test)
            val txtLastTest = view.findViewById<TextViewHeavy>(R.id.txt_last_test)
            val txtNextTest = view.findViewById<TextViewHeavy>(R.id.txt_next_test)
            checkHivTest(txtLastTest,txtNextTest)


        }

        when(preference.getInt(KEYPREFER.HIVSTAT,0)){
            KEYPREFER.POSITIVE -> labresult.inflate()
            KEYPREFER.NEGATIVE -> iamnonhiv.inflate()
            KEYPREFER.IDONTKNOW -> iamnonhiv.inflate()
        }



        ///set Tracked missing
        val data = appDb.getCountNoti()
        if(data.totalNoti != "0" && data.tracked != "0" && data.missing != "0") {
            val total = Integer.parseInt(data.tracked) + Integer.parseInt(data.missing)
            val tracked = (data.tracked.toInt() / total) * 100
            val missing = (data.missing.toInt() / total) * 100
            txt_tracked.text = "$tracked % Tracked"
            txt_missed.text = "$missing % Missed"
        }else{
            txt_tracked.text = "0 % Tracked"
            txt_missed.text = "0 % Missed"
        }
        //

        //display last risk
        if(preference.getLong(KEYPREFER.LASTRISK,0L) != 0L){
            date_riskmeter.text = "Last Risk "+Utils(activity).getDateSlash(Date(preference.getLong(KEYPREFER.LASTRISK,0L)))
        }


    }

    fun checkHivTest(txtLast:TextViewHeavy,txtNext:TextViewHeavy){
        Timber.d { "check HIV test" }
        if (preference.getLong(KEYPREFER.HIVTEST, 0L) != 0L) {
            val currentDate = Date().time
            val hivTestDate = preference.getLong(KEYPREFER.HIVTEST,0L)
            val hivLastTest = preference.getLong(KEYPREFER.HIVLASTTEST,0L)
            if(hivTestDate <= currentDate){
                txtLast.text = Utils(txtLast.context).getDateFormal(Date(hivTestDate))
                val editor = preference.edit()
                editor.putLong(KEYPREFER.HIVLASTTEST, hivTestDate)
                editor.apply()
            }else{
                txtNext.text = Utils(txtNext.context).getDateFormal(Date(hivTestDate))
                txtLast.text = Utils(txtLast.context).getDateFormal(Date(hivLastTest))
            }
        }
    }

    fun initGraph(view :View){

        mChart = view!!.findViewById(R.id.mChart)

        //mChart.setOnChartGestureListener(this)
        //mChart.setOnChartValueSelectedListener(this)
        mChart.setDrawGridBackground(false)

        // no description text
        mChart.description.isEnabled = false

        // enable touch gestures
        mChart.setTouchEnabled(true)

        // enable scaling and dragging
        mChart.isDragEnabled = true
        mChart.setScaleEnabled(true)
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true)

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        //val mv = MyMarkerView(this, R.layout.custom_marker_view)
        //mv.setChartView(mChart) // For bounds control
        //mChart.setMarker(mv) // Set the marker to the chart

        // x-axis limit line
        val llXAxis = LimitLine(10f, "Index 10")
        llXAxis.lineWidth = 4f
        //llXAxis.enableDashedLine(10f, 10f, 0f)
        llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        llXAxis.textSize = 10f

        val xAxis = mChart.xAxis
        xAxis.axisMaximum = 4f
        xAxis.setLabelCount(5,true)
        xAxis.textColor = Color.WHITE
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)

        //sxAxis.enableGridDashedLine(10f, 10f, 0f)
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis) // add_blue x-axis limit line



        val leftAxis = mChart.axisLeft
        leftAxis.removeAllLimitLines() // reset all limit lines to avoid overlapping lines
        leftAxis.textColor = Color.WHITE
        leftAxis.axisMaximum = 150f
        leftAxis.axisMinimum =0f
        leftAxis.setLabelCount(4,true)
        //leftAxis.yOffset = 20f
        //leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawZeroLine(false)
        leftAxis.setDrawGridLines(false)

        // limit lines are drawn behind data (and not on top)
        //leftAxis.setDrawLimitLinesBehindData(true)

        mChart.axisRight.isEnabled = false
        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add_blue data
        setData(5, 100f)

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500)
        //mChart.invalidate();

        // get the legend (only possible after setting_layout data)
        val l = mChart.legend

        // modify the legend ...
        l.form = Legend.LegendForm.LINE
        l.isEnabled =false

        // // dont forget to refresh the drawing
        // mChart.invalidate();

    }


    private fun setData(count: Int, range: Float) {

        val values = ArrayList<Entry>()

        for (i in 0 until count) {

            val `val` = (Math.random() * range).toFloat() + 3
            values.add(Entry(i.toFloat(), `val`))
        }

        val set1: LineDataSet

        if (mChart.data != null && mChart.data.dataSetCount > 0) {
            set1 = mChart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values,"Dataset 1")
            set1.values = values

            set1.setDrawIcons(false)

            // set the line to be drawn like this "- - - - - -"
            //set1.enableDashedLine(10f, 5f, 0f)
            //set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = Color.WHITE
            set1.circleRadius = 5f
            set1.setCircleColor(Color.WHITE)
            set1.lineWidth = 1f
            //set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.setDrawValues(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(false)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            /*
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            */

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add_blue the datasets

            // create a data object with the datasets
            val data = LineData(dataSets)

            // set data
            mChart.data = data
        }
    }



    fun loadUser(id:String){
        manageSub(
                service.getUser(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            dataUser.apply {
                                c.forEach {
                                    item -> add(item)
                                    d { "Add ["+item.name+"] to arraylist" }
                                }
                            }
                            values_point.text = dataUser[0].point+" Points"
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })

        )
    }

    override fun onPause() {
        super.onPause()
        unsubscribe()
    }
}