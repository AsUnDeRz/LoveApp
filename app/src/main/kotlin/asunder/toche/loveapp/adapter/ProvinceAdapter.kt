package asunder.toche.loveapp

import android.databinding.adapters.TextViewBindingAdapter.setText
import asunder.toche.loveapp.R.id.textView
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import asunder.toche.loveapp.Model
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.R.attr.autoText
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.github.ajalt.timberkt.Timber.d


/**
 * Created by ToCHe on 9/17/2017 AD.
 */
class ProvinceAdapter(private val context: Context, private val originalList: ArrayList<searchtext>?) : BaseAdapter(), Filterable {

    data class searchtext(val id:String,val txtTh:String,val txtEN:String,val referId:String,val type:Int)
    private var suggestions = ArrayList<searchtext>()
    //private var suggestions = ArrayList<Model.Province>()
    var utils = Utils(context)

    override fun getCount(): Int {
        return suggestions.size // Return the size of the suggestions list.
    }

    //utils.txtLocale(suggestions[position].province_th,suggestions[position].province_eng)
    override fun getItem(position: Int): Any {
        return utils.txtLocale(suggestions[position].txtTh,suggestions[position].txtEN)

    }


    override fun getItemId(position: Int): Long {
        return suggestions[position].referId.toLong()
    }



    /**
     * This is where you inflate the layout and also where you set what you want to display.
     * Here we also implement a View Holder in order to recycle the views.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val inflater = LayoutInflater.from(context)

        val holder: ViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_province,
                    parent,
                    false)
            holder = ViewHolder()
            holder.autoText = convertView!!.findViewById<TextView>(R.id.txt_province)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.autoText!!.text = utils.txtLocale(suggestions[position].txtTh,suggestions[position].txtEN)
        return convertView
    }

    private class ViewHolder {
        internal var autoText: TextView? = null
    }

    override fun getFilter(): Filter {
        return CustomFilter()
    }



    /**
     * Our Custom Filter Class.
     */
    inner class CustomFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
            suggestions.clear()

            if (originalList != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                for (i in 0 until originalList.size) {
                    if (originalList[i].txtEN.toLowerCase().contains(constraint) || originalList[i].txtTh.toLowerCase().contains(constraint)) {
                        // Compare item in original list if it contains constraints.
                        d{"add "+originalList[i].txtTh}
                        suggestions.add(originalList[i]) // If TRUE add item in Suggestions.
                    }
                }
            }
            val results = Filter.FilterResults() // Create new Filter Results and return this to publishResults;
            results.values = suggestions
            results.count = suggestions.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
            if (results?.count!! > 0) {
                val i =  results?.values as ArrayList<searchtext>
                suggestions = i
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}