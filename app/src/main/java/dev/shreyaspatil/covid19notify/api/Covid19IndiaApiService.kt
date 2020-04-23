package dev.shreyaspatil.covid19notify.api

import dev.shreyaspatil.covid19notify.model.StateDistrictResponse
import dev.shreyaspatil.covid19notify.model.StateResponse
import retrofit2.Response
import retrofit2.http.GET

interface Covid19IndiaApiService {

    @GET("data.json")
    suspend fun getData(): Response<StateResponse>

    @GET("state_test_data.json")
    suspend fun getStatDistrictData(): Response<StateDistrictResponse>

    companion object {
        const val BASE_URL = "https://api.covid19india.org/"
    }
}
