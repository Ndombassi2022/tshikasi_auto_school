package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllLocalByLocalTypeIdByAnyUseCase {
    suspend operator fun invoke(localTypeId:String, value:String): Either<NetworkError, List<LocalModel>>
}