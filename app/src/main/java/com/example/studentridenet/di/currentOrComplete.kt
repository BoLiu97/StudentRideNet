package com.example.studentridenet.di

data class currentOrComplete (
    var depature_location:String = "",
    var arrive_location:String = "",
    var Depature_time:Long = 0L,
    var currentOrComplete:Int = 0,
    //for current this one, 0 represent null, 1 represent current,2 represent complete
    var driverId:Int = 0,
    var oassengerId:Int = 0

)
