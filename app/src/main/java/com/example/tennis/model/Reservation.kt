package com.example.tennis.model

data class Reservation(
    var id: String,
    var day: String,
    var hour: Int,
    var month: String,
    var year: String,
    var plot: String,
    var user: String
){
    constructor(): this("","",0,"","","","")
}
