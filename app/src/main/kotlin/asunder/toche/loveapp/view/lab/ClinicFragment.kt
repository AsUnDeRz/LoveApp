package asunder.toche.loveapp

import android.content.Context
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import asunder.toche.loveapp.R
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.Timber.d
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.clinic_list.*
import kotlinx.android.synthetic.main.header_logo_blue.*



/**
 * Created by admin on 8/2/2017 AD.
 */
class ClinicFragment: Fragment() {

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

    lateinit var utils : Utils
    lateinit var appDb : AppDatabase


    companion object {
        fun newInstance(): ClinicFragment {
            return ClinicFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.clinic_list, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_search.typeface = MyApp.typeFace.medium
        utils = Utils(activity)
        appDb = AppDatabase(activity)
        val province = appDb.getProvince()

        //set title
        title_app.text ="BOOK\nA TEST"
        txt_location.text = LabFragment.city
        txt_location_desc.text = LabFragment.subCity



        btn_map.setOnClickListener {
            ActivityMain.vp_main.setCurrentItem(KEYPREFER.LAB,false)
        }


        rv_clinic_list.layoutManager = GridLayoutManager(context,2)
        rv_clinic_list.setHasFixedSize(true)
        rv_clinic_list.adapter = MasterAdapter.ClinicAdapter(LabFragment.hospitalList,false)

        txt_search.setOnFocusChangeListener { view, b ->
            if(b){
                val proAdapter = ProvinceAdapter(activity, province)
                txt_search.setAdapter(proAdapter)
                txt_search.setOnItemClickListener { adapterView, view, position, id ->
                    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    val v = activity.currentFocus
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    rv_clinic_list.adapter.notifyItemRangeRemoved(0,LabFragment.hospitalList.size-1)

                    province
                            .filter { it.province_id == id.toString() }
                            .forEach {
                                Timber.d { it.province_eng + " // " }
                                loadHospital(it.locx.toString(),it.locy.toString())
                            }


                    Timber.d { "i == $position///l == $id" }
                }
            }
        }

    }

    fun loadHospital(lat:String,lng:String){
        manageSub(
                service.getHospitalsOnMap(lat,lng,"8")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ c -> run {
                            val data = ObservableArrayList<Model.Clinic>().apply{
                                c.forEach {
                                    item -> add(Model.Clinic(item.id.toLong(),utils.txtLocale(item.name_th,item.name_eng),
                                        utils.txtLocale(item.address_th,item.address_eng),utils.txtLocale(item.service_th,item.service_eng),
                                        utils.txtLocale(item.open_hour_th,item.open_hour_eng),item.phone,item.email,item.province,
                                        item.locx,item.locy,item.version,"","","item.hospital_id",
                                        "http://backend.loveapponline.com/"+item.file_path.replace("images","")+"o.png",
                                        "http://backend.loveapponline.com/"+item.file_path+"/"+item.file_name+"_o.png",
                                        if(item.promotion_id != null){item.promotion_id}else{""},if(item.promotion_th != null && item.promotion_eng != null){utils.txtLocale(item.promotion_th,item.promotion_eng)}else{""},
                                        if(item.start_date !=null){utils.getDateSlash(item.start_date)}else{""},
                                        if(item.end_date !=null){utils.getDateSlash(item.end_date)}else{""}))
                                    d { "Add ["+item.name_th+"] to arraylist" }
                                }
                            }
                            //
                            rv_clinic_list.adapter = MasterAdapter.ClinicAdapter(data,false)
                            d { "check response [" + c.size + "]" }
                        }},{
                            d { it.message!! }
                        })
        )
    }
}