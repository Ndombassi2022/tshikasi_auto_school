package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.commune

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CommuneRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllCommuneUseCaseImpl @Inject constructor(
    private val communeRepository: CommuneRepository
) : GetAllCommuneUseCase {
    override suspend fun invoke(): Either<NetworkError, List<CommuneModel>> {
        return  communeRepository.getAllCommune()
    }
}