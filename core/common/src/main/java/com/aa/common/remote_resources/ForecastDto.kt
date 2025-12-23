package com.aa.common.remote_resources

import com.google.gson.annotations.SerializedName

data class ForecastResponseDto(
    @SerializedName("cod") val cod: String?,
    @SerializedName("message") val message: Int?,
    @SerializedName("cnt") val cnt: Int?,
    @SerializedName("list") val list: List<ForecastItemDto>?,
    @SerializedName("city") val city: CityDto?
)


data class ForecastItemDto(
    @SerializedName("dt") val dt: Long?,
    @SerializedName("main") val main: MainForecastDto?,
    @SerializedName("weather") val weather: List<WeatherDescriptionDto>?,
    @SerializedName("clouds") val clouds: CloudsDto?,
    @SerializedName("wind") val wind: WindForecastDto?,
    @SerializedName("visibility") val visibility: Int?,
    @SerializedName("pop") val pop: Double?,
    @SerializedName("sys") val sys: SysDto?,
    @SerializedName("dt_txt") val dtTxt: String?
)


data class MainForecastDto(
    @SerializedName("temp") val temp: Double?,
    @SerializedName("feels_like") val feelsLike: Double?,
    @SerializedName("temp_min") val tempMin: Double?,
    @SerializedName("temp_max") val tempMax: Double?,
    @SerializedName("pressure") val pressure: Int?,
    @SerializedName("sea_level") val seaLevel: Int?,
    @SerializedName("grnd_level") val grndLevel: Int?,
    @SerializedName("humidity") val humidity: Int?,
    @SerializedName("temp_kf") val tempKf: Double?
)

data class WindForecastDto(
    @SerializedName("speed") val speed: Double?,
    @SerializedName("deg") val deg: Int?,
    @SerializedName("gust") val gust: Double?
)


data class CityDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("coord") val coord: CoordDto?,
    @SerializedName("country") val country: String?,
    @SerializedName("population") val population: Int?,
    @SerializedName("timezone") val timezone: Int?,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?
)
