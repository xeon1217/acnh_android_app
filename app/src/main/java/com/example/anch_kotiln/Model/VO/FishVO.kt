package com.example.acnh.Model

data class FishVO (
    var korName: String,
    var engName: String,
    var size: String,
    var sellPrice: String,
    var whereHow: String,
    var weather: String,
    var shadow: String,
    val existFin: Boolean,
    var date: String,
    var time: String
)