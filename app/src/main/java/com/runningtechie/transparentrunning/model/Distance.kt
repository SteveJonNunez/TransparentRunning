package com.runningtechie.transparentrunning.model

class Distance(val meters: Float) {
    //TODO: Calculate only when needed
    val feet = meters/0.3048f
    val miles = meters*0.000621371f
    val kilometers = meters*.001f
    val yards = meters*1.093613298f
}