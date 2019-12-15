package com.example.studentridenet

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.studentridenet.di.offerOrRequest
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.request_fragment.*
import kotlinx.android.synthetic.main.request_fragment.cDate
import kotlinx.android.synthetic.main.request_fragment.eDate
import kotlinx.android.synthetic.main.request_fragment.from
import kotlinx.android.synthetic.main.request_fragment.submit
import kotlinx.android.synthetic.main.request_fragment.to
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private lateinit var viewModel: shareViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(shareViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.listener = this
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var eRecord = ""
        var rgContent = ""

        eDate.setOnClickListener{
            val dpd = DatePickerDialog(context!!,
                DatePickerDialog.OnDateSetListener{ view, myear, mMonth, mDay->
                    eRecord = (""+ mDay + "/"+ mMonth+ "/"+myear )
                    cDate.setText(eRecord)
                },year,month,day)
            dpd.show()
        }
        radio_group?.setOnCheckedChangeListener{_,isChecked->
            when(isChecked){
                R.id.DriveModel->rgContent ="rider"
                R.id.PassengerModel->rgContent ="passenger"

            }
        }
        submit.setOnClickListener{
            println("erecord = "+ eRecord)
            println("radiogroup" + rgContent)
            var id: Int = radio_group1.checkedRadioButtonId
            if (id==-1){
                val text = "please select as driver or passenger"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            } else if(from.text.toString().isEmpty()){
                val text = "please enter your depature location"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }else if(to.text.toString().isEmpty()){
                val text = "please enter your final location"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }else if(cDate.text.toString().isEmpty()){
                val text = "please enter your valide time"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }else {
                viewModel.filtList.value = viewModel.listORR.value!!.filter {
                    it.departure_location == from.text.toString() || it.final_location == to.text.toString() } as ArrayList<offerOrRequest>

                findNavController().navigate(R.id.action_nav_search_to_nav_home)


            }
        }
    }

}
