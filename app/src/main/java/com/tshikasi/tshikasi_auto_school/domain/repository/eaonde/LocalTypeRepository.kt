package com.tshikasi.tshikasi_auto_school.domain.repository.eaonde

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface LocalTypeRepository {
    suspend fun getAllLocalType():Either<NetworkError,List<LocalTypeModel>>
    suspend fun getAllLocalTypeByAny(value: String,limit:Int) :Either<NetworkError, List<LocalTypeModel>>
    suspend fun createLocalType(localTypeModel: LocalTypeModel):Either<NetworkError, LocalTypeModel>
}