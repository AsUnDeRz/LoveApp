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
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import asunder.toche.loveapp.R
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
    lateinit var utils: Utils
    var dataUser = ObservableArrayList<Model.User>()
    var labResultList = ObservableArrayList<Model.RepositoryLabResult>()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.iam, container, false)
        appDb = AppDatabase(activity)
        utils = Utils(activity)
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
            loadLabResult(preference.getString(KEYPREFER.UserId,"0"),view)
            val btnViral = view.findViewById<Button>(R.id.btn_viral_load)
            val btnCd4 = view.findViewById<Button>(R.id.btn_cd4)
            btnViral.setOnClickListener {
                startActivity(Intent().setClass(activity,Cd4VLActivity::class.java))
            }
            btnCd4.setOnClickListener {
                startActivity(Intent().setClass(activity,Cd4VLActivity::class.java))
            }
        }
        iamnonhiv.setOnInflateListener { viewStub, view ->
            d{"i am non hiv inflate"}
            val btnLastTest = view.findViewById<LinearLayout>(R.id.btn_last_test)
            val btnNextTest = view.findViewById<LinearLayout>(R.id.btn_next_test)
            val txtLastTest = view.findViewById<TextViewHeavy>(R.id.txt_last_test)
            val txtNextTest = view.findViewById<TextViewHeavy>(R.id.txt_next_test)
            checkHivTest(txtLastTest,txtNextTest)


        }

        // inflate bottom layout
        when(preference.getInt(KEYPREFER.HIVSTAT,0)){
            KEYPREFER.POSITIVE -> labresult.inflate()
            KEYPREFER.NEGATIVE -> iamnonhiv.inflate()
            KEYPREFER.IDONTKNOW -> iamnonhiv.inflate()
        }



        ///set Tracked missing
        val data = appDb.getCountNoti()
        if(data.totalNoti != "0") {
            val total = Integer.parseInt(data.tracked) + Integer.parseInt(data.missing)
            if(data.tracked.toInt() > 0) {
                val tracked = (data.tracked.toInt() / total) * 100
                txt_tracked.text = "$tracked % Tracked"
            }else{
                txt_tracked.text = "0 % Tracked"

            }
            if(data.missing.toInt() >0) {
                val missing = (data.missing.toInt() / total) * 100
                txt_missed.text = "$missing % Missed"
            }else{
                txt_missed.text = "0 % Missed"

            }
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
        //xAxis.axisMaximum = 4f
        //xAxis.setLabelCount(5,true)
        xAxis.textColor = Color.WHITE
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(true)
        xAxis.valueFormatter = object  :IAxisValueFormatter{
            val mFormat = SimpleDateFormat("dd MMM")
            override fun getFormattedValue(value: Float, axis: AxisBase?): String {
                //val millis = TimeUnit.HOURS.toMillis(value.toLong())
                return mFormat.format(Date(value.toLong()))
            }
        }


        //sxAxis.enableGridDashedLine(10f, 10f, 0f)
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis) // add_blue x-axis limit line


        val leftAxis = mChart.axisLeft
        leftAxis.removeAllLimitLines() // reset all limit lines to avoid overlapping lines
        leftAxis.textColor = Color.WHITE
        //leftAxis.axisMaximum = 150f
        leftAxis.axisMinimum =0f
        leftAxis.setLabelCount(4,true)
        //leftAxis.yOffset = 20f
        //leftAxis.enableGridDashedLine(10f, 10f, 0f)
        leftAxis.setDrawZeroLine(true)
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
        //mChart.invalidate()

        // get the legend (only possible after setting_layout data)
        val l = mChart.legend

        // modify the legend ...
        l.form = Legend.LegendForm.LINE
        l.isEnabled =true
        l.textColor = Color.WHITE
        l.typeface = utils.medium

        // // dont forget to refresh the drawing
        // mChart.invalidate();

    }


    private fun setData(count: Int, range: Float) {

        val viral = ArrayList<Entry>()
        val cd4 = ArrayList<Entry>()

        for(data in labResultList){
            viral.add(Entry(data.test_date.time.toFloat(),data.viral.toFloat()))
            cd4.add(Entry(data.test_date.time.toFloat(),data.cd4.toFloat()))
        }


        val setViral: LineDataSet
        val setCd4 : LineDataSet

        if (mChart.data != null && mChart.data.dataSetCount > 0) {
            setViral = mChart.data.getDataSetByIndex(0) as LineDataSet
            setViral.values = viral
            setCd4 = mChart.data.getDataSetByIndex(1) as LineDataSet
            setCd4.values = cd4
            mChart.data.notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            setViral = LineDataSet(viral,"VIRAL")
            setViral.values = viral

            setViral.setDrawIcons(false)
            // set the line to be drawn like this "- - - - - -"
            //setViral.enableDashedLine(10f, 5f, 0f)
            //setViral.enableDashedHighlightLine(10f, 5f, 0f)
            setViral.color = Color.WHITE
            setViral.valueTextColor = Color.WHITE
            setViral.circleRadius = 5f
            setViral.setCircleColor(Color.WHITE)
            setViral.lineWidth = 1f
            //setViral.circleRadius = 3f
            setViral.setDrawCircleHole(false)
            setViral.setDrawValues(false)
            setViral.valueTextSize = 9f
            setViral.setDrawFilled(false)
            setViral.formLineWidth = 1f
            setViral.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            setViral.formSize = 15f

            //init cd4
            setCd4 = LineDataSet(cd4,"CD4")
            setCd4.values = cd4

            setCd4.setDrawIcons(false)
            // set the line to be drawn like this "- - - - - -"
            // setCd4.enableDashedLine(10f, 5f, 0f)
            // setCd4.enableDashedHighlightLine(10f, 5f, 0f)
            setCd4.color = ContextCompat.getColor(activity,R.color.orange)
            setCd4.valueTextColor = Color.WHITE
            setCd4.circleRadius = 5f
            setCd4.setCircleColor(ContextCompat.getColor(activity,R.color.orange))
            setCd4.lineWidth = 1f
            // setCd4.circleRadius = 3f
            setCd4.setDrawCircleHole(false)
            setCd4.setDrawValues(false)
            setCd4.valueTextSize = 9f
            setCd4.setDrawFilled(false)
            setCd4.formLineWidth = 1f
            setCd4.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            setCd4.formSize = 15f

            /*
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                setViral.fillDrawable = drawable
            } else {
                setViral.fillColor = Color.BLACK
            }
            */

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(setViral) // add_blue the datasets
            dataSets.add(setCd4)

            // create a data object with the datasets
            val data = LineData(dataSets)
            //data.setValueTextSize(9f)
            //data.setDrawValues(true)
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

                            values_point.text = if(dataUser[0].point != null){dataUser[0].point+" Points"}else{""}
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }

    fun loadLabResult(id:String,view:View){
        manageSub(
                service.getLabResult(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data =ObservableArrayList<Model.RepositoryLabResult>().apply {
                                c.forEach {
                                    item -> add(item)
                                    d { "Add ["+item.user_id+"] to arraylist" }
                                }
                            }
                            labResultList = data
                            if(labResultList.size != 0) {
                                initGraph(view)
                            }
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