package com.tshikasi.tshikasi_auto_school.domain.model.eaonde

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.time.Instant

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


@Serializable
@Parcelize
data class LocalTypeModel(
    var id: Long = 0L,
    var description:String="",
    var details:String="",
    var reference:String="",
    var code:String="",
    var status:String="",

    @SerialName("created_at")
    var createdAt : String ?= null,

    @SerialName("activated_at")
    var activatedAt : String ?= null,

    @SerialName("url_image")
    var urlImage:String?= null,

    ):Parcelable


@Serializable
@Parcelize
data class LocalModel(
    @SerialName("id")
    var id: Long = 0,
    @SerialName("local_type_id")
    var localTypeId: Long = 0,
    @SerialName("country_id")
    var countryId: Long = 0,
    @SerialName("description")
    var description: String = "",
    @SerialName("nif")
    var nif: String = "",
    @SerialName("phone")
    var phone: String = "",
    @SerialName("mobile")
    var mobile: String = "",
    @SerialName("email")
    var email: String = "",
    @SerialName("address")
    var address: String = "",
    @SerialName("latitude")
    var latitude: String = "",
    @SerialName("longitude")
    var longitude: String = "",
    @SerialName("details")
    var details: String = "",
    @SerialName("url_image")
    var urlImage: String = "",
    @SerialName("logo")
    var logo: String = "",
    @SerialName("status")
    var status: String = "",
    @Contextual
    @SerialName("updated_at")
    var updatedAt: Instant = Instant.EPOCH,
    @Contextual
    @SerialName("created_at")
    var createdAt: Instant = Instant.EPOCH,
    @SerialName("reference")
    var reference: String = "",
    @SerialName("tb_local_type")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var localType: LocalTypeModel? = null,
    @SerialName("tb_country")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    var country: CountryModel? = null,
):Parcelable

@Serializable
@Parcelize
data class LoginLocalModel(
    val id : Long = 0L,
    val reference : String= "",
    val password:String="",
    val status: String="",

    @SerialName("user_name")
    val userName: String="",
    @SerialName("local_id")
    val localId: Long=0,

    @Transient
    val local: LocalModel = LocalModel()
): Parcelable