package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.commune

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface GetAllCommuneByAnyUseCase {
    suspend operator fun invoke(value : String):Either<NetworkError, List<CommuneModel>>
}