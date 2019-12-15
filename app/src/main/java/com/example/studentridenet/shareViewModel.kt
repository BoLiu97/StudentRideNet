package com.example.studentridenet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.studentridenet.di.offerOrRequest
import com.example.studentridenet.di.user
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class shareViewModel(application: Application) : AndroidViewModel(application) {
    //user
    //triplist
    //offer list


    var database = MutableLiveData<DatabaseReference>()

    //var curORR = MutableLiveData<currentOrComplete>()// one Current trip in trip frag

    var filtList = MutableLiveData<ArrayList<offerOrRequest>>()

    var curOORtrip = MutableLiveData<offerOrRequest>() // one Current trip in trip frag

    var curOOR = MutableLiveData<offerOrRequest>() // one offer and request

    var listORR = MutableLiveData<ArrayList<offerOrRequest>>() // list of offer and request

    var listTrip = MutableLiveData<ArrayList<offerOrRequest>>() // list of user personal trip

    var listofDriver = MutableLiveData<ArrayList<offerOrRequest>>() // list of offer

    var listofPassenger = MutableLiveData<ArrayList<offerOrRequest>>() // list of request

    var curUser = MutableLiveData<user>()
    var listofUser = MutableLiveData<ArrayList<user>>()

    var facebookname = MutableLiveData<String>()
    var facebookemail = MutableLiveData<String>()
    var facebookid = MutableLiveData<String>()

    init {
        facebookid.value =""
        //curORR.value = currentOrComplete()
        curOOR.value = offerOrRequest()
        curOORtrip.value = offerOrRequest()

        curUser.value = user()

        filtList.value = ArrayList()
        listORR.value = ArrayList()
        listTrip.value = ArrayList()
        listofUser.value = ArrayList()

        listofDriver.value = ArrayList()
        listofPassenger.value = ArrayList()

        database.value = FirebaseDatabase.getInstance().reference

        database.value?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                listORR.value!!.clear()
                listTrip.value!!.clear()
                //userList.value!!.clear()
                //we will have three lists ont for basic information one for all of the ORR one for user COC

                var userN = arrayListOf<String>()

                p0.child("offerorRequest").children.forEach {
                    it.getValue(offerOrRequest::class.java)?.let {
//                        userN.add(it.user_id)
                        listORR.value!!.add(it)
                        //nameList.value?.add(it)
                    }
                }

                p0.child("user").child(curOOR.value!!.user_id).child("trip")
                    .children.forEach {
                    //need to get ride of the class value same as previous one)
                    it.getValue(offerOrRequest::class.java)?.let {
                        //                        userN.add(it.user_id)
                        listTrip.value!!.add(it)
                        //nameList.value?.add(it)
                    }
                }



                listener?.updateRecyclerView()
            }
        })
    }
    var listener: DataChangedListener? = null
    interface DataChangedListener {
        fun updateRecyclerView()
    }

    fun uploadofferorreuqest() {
    val sId = curOOR.value!!.offer_id.toString()
    database.value?.child("offerorRequest")?.child(sId)!!.setValue(curOOR.value)

}
    fun uploadUsertrip(){
        val sId = curOOR.value!!.user_id
        val ssId = curOOR.value!!.offer_id.toString()

        database.value?.child("user")?.child(sId)!!
            .child("trip").child(ssId).setValue(curOOR.value)
    }

    fun regeisterUser(){
        val sId = curOOR.value!!.user_id
        println("curOOR number " + curOOR.value!!.user_id)

        var aa = listTrip.value?.filter { it.user_id == sId }
        println("lisr trip " + listTrip.value)



        database.value?.child("user")?.child(sId)?.setValue(curUser.value)
    }

    fun removeTrip(index: Int){
   //     println("size of personal list"  + listTrip.value!!.size)

            val idd = listTrip.value?.get(index)!!.offer_id

            var iddd = idd.toString()

            database.value?.child("offerorRequest")
                ?.child(iddd)
                ?.removeValue()


        listTrip.value?.removeAt(index)
        println("list content = " + listTrip.value!! )

    }



}