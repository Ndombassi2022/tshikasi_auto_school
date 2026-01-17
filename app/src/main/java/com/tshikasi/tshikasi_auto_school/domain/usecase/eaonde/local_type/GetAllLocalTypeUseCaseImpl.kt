package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.local_type

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.LocalTypeModel
import com.tshikasi.tshikasi_auto_school.domain.repository.eaonde.LocalTypeRepository
import com.tshikasi.tshikasi_auto_school.utils.NetworkError
import javax.inject.Inject

class GetAllLocalTypeUseCaseImpl @Inject constructor(private val localTypeRepository: LocalTypeRepository):
    GetAllLocalTypeUseCase {
    override suspend fun invoke(): Either<NetworkError, List<LocalTypeModel>> {
          return  localTypeRepository.getAllLocalType()
    }
}