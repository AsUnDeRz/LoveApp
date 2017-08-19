package asunder.toche.loveapp

import android.app.Activity
import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.text.SpannableString
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by admin on 8/19/2017 AD.
 */
class InfoMapAdapter(var dataClinic:ObservableArrayList<Model.Clinic>,context: Activity) : InfoWindowAdapter {

    private val mContents: View= context.layoutInflater.inflate(R.layout.custom_marker, null)
    init {
    }

    override fun getInfoWindow(marker: Marker): View? {
        render(marker,mContents)
        return mContents
    }

    override fun getInfoContents(marker: Marker): View? {
        render(marker, mContents)
        return mContents
    }

    private fun render(marker: Marker, view: View) {
        var badge: Int =0

        val title = marker.title
        val titleUi = view.findViewById<TextView>(R.id.txt_name)
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            val titleText = SpannableString(title)
            //titleText.setSpan(ForegroundColorSpan(Color.RED), 0, titleText.length, 0)
            titleUi.text = titleText
        } else {
            titleUi.text = ""
        }

        dataClinic
                .filter { it.name == title }
                .forEach { badge = it.icon }

        view.findViewById<CircleImageView>(R.id.profile_image).setImageResource(badge)

        val snippet = marker.snippet
        val snippetUi = view.findViewById<TextView>(R.id.txt_desc)
        if (snippet != null) {
            val snippetText = SpannableString(snippet)
            //snippetText.setSpan(ForegroundColorSpan(Color.MAGENTA), 0, 10, 0)
            //snippetText.setSpan(ForegroundColorSpan(Color.BLUE), 12, snippet.length, 0)
            snippetUi.text = snippetText
        } else {
            snippetUi.text = ""
        }
    }
}