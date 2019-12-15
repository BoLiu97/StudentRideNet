package com.example.studentridenet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentridenet.di.offerOrRequest
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import kotlinx.android.synthetic.main.fragment_passenger.*
import kotlinx.android.synthetic.main.hori_trip.view.*
import org.json.JSONArray
import org.json.JSONObject

class PassengerFragment : Fragment() {

    lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: shareViewModel
    private lateinit var viewAdapter: homeRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(shareViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.listener = this
        return inflater.inflate(R.layout.fragment_passenger, container, false)
    }

    fun updateRecyclerView() {
        //viewAdapter.OOrRData = viewModel.listTrip.value!!
        viewAdapter.notifyDataSetChanged()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var token = AccessToken.getCurrentAccessToken()

        useLoginInformation(token)


        viewManager = LinearLayoutManager(context)

        viewAdapter =
            homeRecyclerViewAdapter(
                viewModel.listORR.value!!
            ) { oor: offerOrRequest ->
                recyclerViewItemSelected(oor)
            }

        passenger_recycler.apply {
            this.layoutManager = viewManager
            this.adapter = viewAdapter
        }

//        var pList = viewModel.listORR.value!!.filter{it.driverOrPassenger==2}
//        println("listorr" + viewModel.listORR.value!!)
//
//        println("pass list " + pList)

        viewModel.listORR.observe(this, Observer {
            var pList = it.filter{it.driverOrPassenger==2}
            viewAdapter.list = pList as ArrayList<offerOrRequest>
            print("pass list " + pList)
            viewAdapter.notifyDataSetChanged()
        })



    }


    class homeRecyclerViewAdapter(var list: ArrayList<offerOrRequest>, val clickListener: (offerOrRequest)->Unit) :
        RecyclerView.Adapter<homeRecyclerViewAdapter.RecyclerViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val viewItem =
                LayoutInflater.from(parent.context).inflate(R.layout.hori_trip, parent, false)
            return RecyclerViewHolder(
                viewItem
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            holder.bind(list[position], clickListener)
        }

        class RecyclerViewHolder(val viewItem: View) : RecyclerView.ViewHolder(viewItem) {

            fun bind(oRR: offerOrRequest, clickListener: (offerOrRequest) -> Unit) {
                viewItem.run {
                    findViewById<TextView>(R.id.destination_text).text = oRR.departure_location
                    findViewById<TextView>(R.id.departure_text).text = oRR.final_location
                    findViewById<TextView>(R.id.name_text).text = oRR.user_name
                    findViewById<TextView>(R.id.date).text = oRR.pickup_date
                    findViewById<TextView>(R.id.time).text = oRR.pickup_time
                    Glide.with(this).load("https://graph.facebook.com/${oRR.user_id}/picture?type=normal").into(IV_head)

                    viewItem.msg.setOnClickListener {
                        clickListener(oRR)
                    }
                }
            }
        }

    }
    private fun recyclerViewItemSelected(offerOrRequest: offerOrRequest) {
        if(offerOrRequest.email.isNullOrEmpty()){
            val text = "User does not have email"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
        }else{
            val recipient = offerOrRequest.email.trim()
            val subject = "questions for the trip to ${offerOrRequest.final_location} ".trim()
            val message = "Hi ${offerOrRequest.user_name}:".trim()
            sendEmail(recipient, subject, message)
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
    fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

    }
}