package com.androidstudio.kotlin.pockemon

class Pockemon{
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
    var lat:Double?=null
    var log:Double?=null
    var isCatch:Boolean?=false

    constructor(
        name: String?,
        des: String?,
        image: Int?,
        power: Double?,
        lat: Double?,
        log: Double?
    ) {
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        this.lat = lat
        this.log = log
        this.isCatch= false
    }


}