package toche.asunder.loveapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import kotlinx.android.synthetic.main.lab_result.*
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import android.graphics.DashPathEffect
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_main.*


class ActivityMain : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bnve.enableAnimation(false)
        bnve.enableShiftingMode(false)
        bnve.enableItemShiftingMode(false)
        bnve.setIconsMarginTop(30)

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
        llXAxis.labelPosition = LimitLabelPosition.RIGHT_TOP
        llXAxis.textSize = 10f

        val xAxis = mChart.xAxis
        xAxis.axisMaximum = 4f
        xAxis.setLabelCount(5,true)
        xAxis.textColor = Color.WHITE
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)

        //sxAxis.enableGridDashedLine(10f, 10f, 0f)
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis) // add x-axis limit line



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

        // add data
        setData(5, 100f)

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500)
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        val l = mChart.legend

        // modify the legend ...
        l.form = LegendForm.LINE
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
            dataSets.add(set1) // add the datasets

            // create a data object with the datasets
            val data = LineData(dataSets)

            // set data
            mChart.data = data
        }
    }


}
