package dev.shreyaspatil.covid19notify.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

//https://api.covid19india.org/v2/state_district_wise.json
//New Updated Response from the api
/*"state": "Maharashtra",
"statecode": "MH",
"districtData": [
{
    "district": "Other States",
    "notes": "Cases from other States/UTs",
    "active": 24,
    "confirmed": 28,
    "deceased": 4,
    "recovered": 0,
    "delta": {
    "confirmed": 1,
    "deceased": 0,
    "recovered": 0
}
},
{
    "district": "Ahmednagar",
    "notes": "",
    "active": 17,
    "confirmed": 43,
    "deceased": 2,
    "recovered": 24,
    "delta": {
    "confirmed": 1,
    "deceased": 0,
    "recovered": 0
}
},*/

@Parcelize
@JsonClass(generateAdapter = true)
data class StateDistrictDetails(
    val districtData: List<DistrictData>,
    val state: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class DistrictData(
    val confirmed: Int = 0,
    val active: Int = 0,
    val deceased: Int = 0,
    val recovered: Int = 0,
    val delta: Delta,
    val district: String = ""
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class Delta(
    val confirmed: Int = 0,
    val deceased: Int = 0,
    val recovered: Int = 0
) : Parcelable