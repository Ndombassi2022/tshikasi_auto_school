package com.tshikasi.tshikasi_auto_school.domain.model.eaonde

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Parcelize
data class LocalServiceAccessModel(
    val id : String = "",
    val status : String = "",
    @SerialName("activated_date")
    val activatedDate : String = "",
    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null,

    @SerialName("local_service_access_type_id")
    val localServiceAccessTypeId : Long=0L,

    @SerialName("local_id")
    val localId : Long=0L,

    @Transient
    val localServiceAccessType: LocalServiceAccessTypeModel = LocalServiceAccessTypeModel(),
    @Transient
    val local: LocalModel = LocalModel()
):Parcelable