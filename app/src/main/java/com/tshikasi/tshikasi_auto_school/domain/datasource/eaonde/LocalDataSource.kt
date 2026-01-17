package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel


interface LocalDataSource {
    suspend fun getAllLocal():List<LocalModel>
    suspend fun updateLocal(localModel: LocalModel) : LocalModel
    suspend fun updateLocalLocation(localModel: LocalModel) : LocalModel
    suspend fun createLocal(localModel: LocalModel): LocalModel
    suspend fun getAllLocalByLocalType(value: String) :  List<LocalModel>
    suspend fun getAllLocalByReference(value: String) : LocalModel
    suspend fun getAllLocalByAny(value: String) : List<LocalModel>
    suspend fun getAllLocalByLocalTypeIdByAny(localTypeId:String,value: String) :  List<LocalModel>
    suspend fun getLocalById(id: String) : LocalModel
}