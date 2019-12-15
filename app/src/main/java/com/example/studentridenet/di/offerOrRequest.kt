package com.example.studentridenet.di

data class offerOrRequest(
    var offer_id: Int = 0,
    var user_id: String= "",
    var departure_location:String = "",
    var final_location:String = "",
    var pickup_date:String = "",
    var pickup_time:String = "",
    var driverOrPassenger:Int = 0,
    var user_name:String="",
    var email:String=""

    // offerorRequest: 0 represent null, 1 represnet driver,2 represent passager
)
