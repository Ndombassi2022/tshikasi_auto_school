package com.tshikasi.tshikasi_auto_school.domain.model.eaonde

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
class Models {
}

//Continent
@Serializable
@Parcelize
data class ContinentModel(
    val id : Long=0L,
    val description : String =""
):Parcelable

//Country
@Serializable
@Parcelize
data class CountryModel @OptIn(ExperimentalSerializationApi::class) constructor(
    val id : Long = 0L,
    val description : String= "",
    @SerialName("created_at")
    val continentId:Long=0L,

    @SerialName("tb_continent")
    @EncodeDefault(EncodeDefault.Mode.NEVER) // 🚀 ignora no insert/update
    val continent: ContinentModel? = null
):Parcelable

//Province
@Serializable
@Parcelize
data class ProvinceModel(
    val id : Long=0L,
    val description : String= "",
    @SerialName("country_id")
    val countryId : Long=0L,
    @Transient
    val country: CountryModel = CountryModel()
): Parcelable


//Municipality
@Serializable
@Parcelize
data class MunicipalityModel(
    val id : Long=0L,
    val description : String= "",
    @SerialName("province_id")
    val provinceId : Long=0L,
    @Transient
    val province: ProvinceModel = ProvinceModel()
): Parcelable

//Commune
@Serializable
@Parcelize
data class CommuneModel(
    val id :Long=0L,
    val description : String= "",
    @SerialName("municipality_id")
    val municipalityId:Long=0L,
    @Transient
    val municipality: MunicipalityModel = MunicipalityModel()
): Parcelable

