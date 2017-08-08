package asunder.toche.loveapp

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by rakshakhegde on 01/02/17.
 */
class LastPagerAdapter(private val modelId: Int) : PagerAdapter() {

	private val pagerItems = ObservableArrayList<PagerItem>()
	private var layoutInflater: LayoutInflater? = null
	private val INFLATER_UNINITIALIZED_MSG = "LayoutInflater is not initialized!" +
			" Please pass ViewPager instance using into()."

	init {
		pagerItems.onListChanged { notifyDataSetChanged() }
	}

	@JvmOverloads
	fun add(layoutId: Int, title: CharSequence? = null, model: Any? = null,
            width: Float = DEFAULT_WIDTH, adapter : LastAdapter? = null, layoutManager: RecyclerView.LayoutManager) = apply {
		pagerItems.add(PagerItem(layoutId, model, title, width,adapter,layoutManager))
	}



	// TODO Unable to overload title with String res Id instead of just CharSequence

	fun remove(position: Int) {
		pagerItems.removeAt(position)
	}

	fun into(viewPager: ViewPager) {
		layoutInflater = LayoutInflater.from(viewPager.context)
		viewPager.adapter = this@LastPagerAdapter
	}

	override fun getCount(): Int = pagerItems.size

	override fun instantiateItem(container: ViewGroup, position: Int): Any =
			pagerItems[position].apply {

				binding?.let {
					container.addView(it.root)
				} ?: run {

					val view = checkNotNull(layoutInflater) { INFLATER_UNINITIALIZED_MSG }
							.inflate(layoutId, container, false)

					binding = DataBindingUtil.bind<ViewDataBinding>(view).apply {
						setVariable(modelId, model)
						executePendingBindings()
					}
                    //val rvContent = view.findViewById(R.id.rv_myaccount) as RecyclerView
                    //rvContent.setHasFixedSize(true)
                    //rvContent.layoutManager = pagerItems[position].layoutManager
                    //rvContent.adapter = pagerItems[position].adapter

					container.addView(view)
				}
			}

	override fun isViewFromObject(view: View, obj: Any): Boolean =
			view == ((obj as PagerItem).binding?.root as View)

	override fun getItemPosition(obj: Any?): Int {
		val position = pagerItems.indexOf(obj as PagerItem)
		return if (position == -1) POSITION_NONE else position
	}

	override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
		(obj as PagerItem).apply {
			//			saveHierarchyState()
			container.removeView(binding?.root)
		}
	}

	override fun getPageTitle(position: Int) = pagerItems[position].title

	override fun getPageWidth(position: Int) = pagerItems[position].width
}
