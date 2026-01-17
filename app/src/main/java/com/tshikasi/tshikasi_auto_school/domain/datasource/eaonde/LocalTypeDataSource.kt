package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel


interface LocalTypeDataSource {
    suspend fun getAllLocalType():List<LocalTypeModel>
    suspend fun getAllLocalTypeByAny(value: String,limit:Int) : List<LocalTypeModel>
    suspend fun createLocalType(localTypeModel: LocalTypeModel): LocalTypeModel
}