package com.tshikasi.tshikasi_auto_school.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun getCurrenteDistance(startLatLng : LatLng, endLatLng : LatLng):Float{


    val startLocation : Location = Location("startLatLng")
    val endLocation : Location = Location("endLatLng")

    startLocation.latitude = startLatLng.latitude
    startLocation.longitude = startLatLng.longitude

    endLocation.latitude = endLatLng.latitude
    endLocation.longitude = endLatLng.longitude

    // var distance : Float = startLocation.distanceTo(endLocation)
    return  startLocation.distanceTo(endLocation)
}

fun getCalculateValueByKilometer(distance: Float):Double{
    val valueByKilometer : Double = 300.0
    val distanceValue = distance / 1000

    println("DISTANCE "+distance)
    println("DISTANCE2 "+distanceValue)

    if(distanceValue < 5.0 ){
        val value = (valueByKilometer * 100)  /100

        println("PORCENTAGE "+value)
        return  value * distanceValue
    }else  if(distanceValue > 5.0 && distanceValue < 10.0){
        val value =  (valueByKilometer * 90)  /100
        return  value * distanceValue
    }else  if(distanceValue > 10.0 && distanceValue < 15.0){
        val value =  (valueByKilometer * 80)  /100
        return  value * distanceValue
    }else  if(distanceValue > 15.0 && distanceValue < 25.0){
        val value = (valueByKilometer * 70)  /100
        return  value * distanceValue
    }else  if(distanceValue > 25.0 && distanceValue < 35.0){
        val value =  (valueByKilometer * 60)  /100
        return  value * distanceValue
    }else  if(distanceValue > 35.0 && distanceValue < 45.0){
        val value =  (valueByKilometer * 50)  /100
        return  value * distanceValue
    }else{
        val value = (valueByKilometer * 50)  /100
        return  value * distanceValue
    }

}
