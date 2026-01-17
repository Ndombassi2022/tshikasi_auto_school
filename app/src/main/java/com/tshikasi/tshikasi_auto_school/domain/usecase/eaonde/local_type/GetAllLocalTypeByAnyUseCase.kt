package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllLocalTypeByAnyUseCase {
    suspend operator fun invoke(value: String,limit:Int):Either<NetworkError, List<LocalTypeModel>>
}