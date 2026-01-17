package com.tshikasi.tshikasi_auto_school.domain.model.eaonde

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Parcelize
data class LocalServiceModel(
    val id:Long=0,
    val description:String="",
    val details: String="",
    val reference:String="",

    @SerialName("video_url")
    val videoUrl: String = "",

    @SerialName("url_image")
    val urlImage : String = "",

    @SerialName("local_id")
    val localId : Long = 0L,

   @Transient
    val local: LocalModel = LocalModel(),
):Parcelable