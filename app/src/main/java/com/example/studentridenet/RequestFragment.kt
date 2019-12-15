package com.example.studentridenet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.example.studentridenet.di.offerOrRequest
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import kotlinx.android.synthetic.main.request_fragment.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class RequestFragment : Fragment() {

    companion object {
        fun newInstance() = RequestFragment()
    }

    private lateinit var viewModel: shareViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(shareViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.listener = this
        return inflater.inflate(R.layout.request_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // all if statement for empty plant
        // change plain text parts from now showing text to hidden text
        //

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)

        var userid = ""

        var eRecord = ""
        var tRecord = ""
        var rgContent = 0

        var token = AccessToken.getCurrentAccessToken()

        useLoginInformation(token)



        Log.d("oor", userid)

        eDate.setOnClickListener{
            val dpd = DatePickerDialog(context!!,DatePickerDialog.OnDateSetListener{view,myear,mMonth,mDay->
                eRecord = (""+ mDay + "/"+ mMonth+ "/"+myear )
                cDate.setText(eRecord)
            },year,month,day)
            dpd.show()
        }

        pickTimeBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
                tRecord = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(context!!, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        radio_group.setOnCheckedChangeListener{_,isChecked->
            when(isChecked){
                R.id.ride->rgContent =1 //1 represent rider
                R.id.Drive->rgContent =2 //2 represent passenger

            }
        }

        submit.setOnClickListener{
            println("erecord = "+ eRecord)
            println("trecord = "+ tRecord)
            println("radiogroup" + rgContent)

            var id: Int = radio_group.checkedRadioButtonId
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
                val id =Random().nextInt(1000000000)

                viewModel.curOOR.value = offerOrRequest(//need user name
                    offer_id = id, departure_location = from.text.toString()
                    ,final_location =  to.text.toString()
                    ,pickup_date= eRecord,pickup_time= tRecord,driverOrPassenger= rgContent ,
                    user_id =  viewModel.facebookid.value!!,user_name =viewModel.facebookname.value!!,
                    email = viewModel.facebookemail.value!!)

                viewModel.uploadofferorreuqest()
                //viewModel.uploadUsertrip()
                findNavController().navigate(R.id.action_global_nav_trip)


                //then upload it.

            }

        }

    }
    private fun useLoginInformation(accessToken: AccessToken) {


        val request = GraphRequest.newMeRequest(
            accessToken,
            object : GraphRequest.GraphJSONArrayCallback, GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(objects: JSONArray?, response: GraphResponse?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onCompleted(jobject: JSONObject?, response: GraphResponse?) {

                    if (jobject != null) {
                        Log.d("object1", jobject.toString())

                        viewModel.facebookid.value = jobject.getString("id")
                        viewModel.facebookname.value = jobject.getString("name")
                        viewModel.facebookemail.value = jobject.getString("email")

                        Log.d("oor", viewModel.curOOR.value!!.user_id)

                    }

                }
            }
        )


        val parameters = Bundle()
        parameters.putString("fields", "id , name  , email")
        request.parameters = parameters
        request.executeAsync()

    }


}
