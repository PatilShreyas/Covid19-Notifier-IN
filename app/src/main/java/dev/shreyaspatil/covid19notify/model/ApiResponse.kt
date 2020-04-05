package dev.shreyaspatil.covid19notify.model

import com.squareup.moshi.Json

data class ApiResponse(
    @Json(name = "statewise")
    val stateWiseDetails: List<Details>
)