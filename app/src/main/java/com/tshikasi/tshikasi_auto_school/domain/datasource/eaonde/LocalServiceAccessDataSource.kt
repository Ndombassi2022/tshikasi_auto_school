package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceAccessModel

interface LocalServiceAccessDataSource {
    suspend fun createLocalServiceAccess(localServiceAccessModel: LocalServiceAccessModel): LocalServiceAccessModel
    suspend fun getAllLocalServiceAccess(): List<LocalServiceAccessModel>
    suspend fun getAllLocalServiceAccessByLocalId(localId: String): List<LocalServiceAccessModel>
    suspend fun getAllLocalServiceAccessById(id: String): LocalServiceAccessModel
}