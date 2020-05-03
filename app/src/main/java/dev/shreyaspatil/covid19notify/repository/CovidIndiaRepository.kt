package dev.shreyaspatil.covid19notify.repository

import dev.shreyaspatil.covid19notify.api.Covid19IndiaApiService
import dev.shreyaspatil.covid19notify.model.StateDistrictDetails
import dev.shreyaspatil.covid19notify.model.StateResponse
import dev.shreyaspatil.covid19notify.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

@ExperimentalCoroutinesApi
class CovidIndiaRepository(private val apiService: Covid19IndiaApiService) {

    fun getData(): Flow<State<StateResponse>> {
        return object : NetworkBoundRepository<StateResponse>() {
            override suspend fun fetchFromRemote(): Response<StateResponse> = apiService.getData()
        }.asFlow().flowOn(Dispatchers.IO)
    }

    //State District Data
    fun getStateDistrictData(): Flow<State<List<StateDistrictDetails>>> {
        return object : NetworkBoundRepository<List<StateDistrictDetails>>() {
            override suspend fun fetchFromRemote(): Response<List<StateDistrictDetails>> =
                apiService.getStateDistrictData()
        }.asFlow().flowOn(Dispatchers.IO)
    }
}