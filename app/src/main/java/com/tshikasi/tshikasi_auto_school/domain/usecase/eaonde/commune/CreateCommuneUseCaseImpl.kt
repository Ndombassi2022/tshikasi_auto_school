package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.commune

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.CommuneModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.CommuneRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class CreateCommuneUseCaseImpl @Inject constructor(
    private val communeRepository: CommuneRepository
): CreateCommuneUseCase {
    override suspend fun invoke(communeModel: CommuneModel): Either<NetworkError, CommuneModel> {
        return  communeRepository.createCommune(communeModel = communeModel)
    }
}