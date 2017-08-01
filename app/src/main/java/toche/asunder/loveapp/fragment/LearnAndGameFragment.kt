package toche.asunder.loveapp.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import toche.asunder.loveapp.R

/**
 * Created by admin on 8/1/2017 AD.
 */
class LearnAndGameFragment : Fragment() {

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

    }
}