package com.tshikasi.tshikasi_auto_school.domain.datasource.eaonde

import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalServiceModel
interface LocalServiceDataSource {
    suspend fun createLocalService(localServiceModel: LocalServiceModel): LocalServiceModel
    suspend fun getAllLocalService():List<LocalServiceModel>
    suspend fun getAllLocalServiceByLocal(value: String):List<LocalServiceModel>
    suspend fun updateLocalService(localServiceModel: LocalServiceModel): LocalServiceModel
}