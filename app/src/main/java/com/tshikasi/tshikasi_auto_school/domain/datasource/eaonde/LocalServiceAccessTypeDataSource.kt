package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessTypeModel

interface LocalServiceAccessTypeDataSource {
    suspend  fun  createLocalServiceAccessType(localServiceAccessTypeModel: LocalServiceAccessTypeModel):LocalServiceAccessTypeModel
    suspend fun getAllLocalServiceAccessType():  List<LocalServiceAccessTypeModel>
    suspend fun getLocalServiceAccessTypeById(id : String):  LocalServiceAccessTypeModel
}