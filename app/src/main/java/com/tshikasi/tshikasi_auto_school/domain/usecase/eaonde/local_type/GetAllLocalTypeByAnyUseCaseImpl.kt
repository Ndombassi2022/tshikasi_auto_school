package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalTypeRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllLocalTypeByAnyUseCaseImpl @Inject constructor(
    private val localTypeRepository: LocalTypeRepository
) : GetAllLocalTypeByAnyUseCase {
    override suspend fun invoke(value: String,limit:Int): Either<NetworkError, List<LocalTypeModel>> {
        return localTypeRepository.getAllLocalTypeByAny(value = value, limit = limit)
    }
}