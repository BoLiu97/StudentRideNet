package com.example.studentridenet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.studentridenet.di.offerOrRequest
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import kotlinx.android.synthetic.main.fragment_trip.*
import kotlinx.android.synthetic.main.hori_trip.view.*
import org.json.JSONArray
import org.json.JSONObject

class TripFragment : Fragment(), shareViewModel.DataChangedListener {


    lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewModel: shareViewModel
    private lateinit var viewAdapter: RecyclerViewAdapter

    override fun updateRecyclerView() {
        viewModel.listTrip.observe(this, Observer {
            viewAdapter.list = it
        })
        viewAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(shareViewModel::class.java)
        } ?: throw Exception("bad activity")
        //viewModel.listener = this
        return inflater.inflate(R.layout.fragment_trip, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var token = AccessToken.getCurrentAccessToken()
        useLoginInformation(token)

        println("this is token in trip fragment" + token)

        viewModel.listTrip.value =
            viewModel.listORR.value!!.filter { it.user_id == viewModel.curUser.value!!.user_id } as ArrayList<offerOrRequest>

        viewManager = LinearLayoutManager(context)
        viewAdapter =
            RecyclerViewAdapter(viewModel.listTrip.value!!) { oor: offerOrRequest ->
                recyclerViewItemSelected(oor)
            }

        trip_recycleview.apply {
            this.layoutManager = viewManager
            this.adapter = viewAdapter
        }
        viewModel.listTrip.observe(this, Observer {
            viewAdapter.list = it

            viewAdapter.notifyDataSetChanged()
        })
        println("test drum list " + viewModel.listTrip.value)
        println("test user id" + viewModel.curOOR.value!!.user_id)

        ItemTouchHelper(SwipeHandler()).attachToRecyclerView(
            trip_recycleview
        )

    }

    fun recyclerViewItemSelected(orRequest: offerOrRequest) {
        //viewModel.curOORtrip.value = orRequest
    }

    inner class SwipeHandler() : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            viewModel.removeTrip(viewHolder.adapterPosition)
            viewAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            val text = "You have deleted this trip!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
            viewAdapter.notifyDataSetChanged()

        }


    }


    class RecyclerViewAdapter(
        var list: ArrayList<offerOrRequest>,
        val clickListener: (offerOrRequest) -> Unit
    ) :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
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


                    Glide.with(this)
                        .load("https://graph.facebook.com/${oRR.user_id}/picture?type=normal")
                        .into(IV_head);

                }
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

//}
}