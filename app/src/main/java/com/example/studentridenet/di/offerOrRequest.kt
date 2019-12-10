package com.example.studentridenet.di

data class offerOrRequest(
    var offer_id: Int = 0,
    var pickup_location:String = " ",
    var final_location:String = " ",
    var time:Long = 0L,
    var price: Double = 0.0,
    var offerOrRequest:Int = 0
    // offerorRequest: 0 represent null, 1 represnet offer,2 represent request
)
