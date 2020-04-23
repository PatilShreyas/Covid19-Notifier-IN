package dev.shreyaspatil.covid19notify.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

//https://api.covid19india.org/v2/state_district_wise.json
/*
[
{
    "state": "Kerala",
    "districtData": [
    {
        "district": "Thrissur",
        "confirmed": 12,
        "lastupdatedtime": "",
        "delta": {
        "confirmed": 0
    }
    },
    {
        "district": "Alappuzha",
        "confirmed": 3,
        "lastupdatedtime": "",
        "delta": {
        "confirmed": 0
    }
    }
    ],*/
@Parcelize
data class StateDistrictResponse(
    @Json(name = "o")
    val stateDistrictResponse: List<StateDistrictDetails>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class StateDistrictDetails(
    val districtData: List<DistrictData>,
    val state: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class DistrictData(
    val confirmed: Int,
    val delta: Delta,
    val district: String,
    val lastupdatedtime: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Delta(
    val confirmed: Int
) : Parcelable