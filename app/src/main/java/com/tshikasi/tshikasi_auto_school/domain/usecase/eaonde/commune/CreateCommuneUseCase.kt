package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.commune

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateCommuneUseCase {
    suspend operator fun  invoke(communeModel: CommuneModel): Either<NetworkError, CommuneModel>
}