package com.tshikasi.tshikasi_auto_school.domain.usecase.eaonde.municipality

import arrow.core.Either
import com.tshikasi.tshikasi_auto_school.domain.model.eaonde.MunicipalityModel
import com.tshikasi.tshikasi_auto_school.utils.NetworkError

interface CreateMunicipalityUseCase {
    suspend operator fun invoke(municipalityModel: MunicipalityModel):Either<NetworkError, MunicipalityModel>
}