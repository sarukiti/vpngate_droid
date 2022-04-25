package dev.planetdisk.vpngate_droid

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

private const val BASE_URL =
    "https://www.vpngate.net"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()
interface VpnGateCsvService {
    @GET("/api/iphone/")
    suspend fun getCsv(): String
}
interface VpnGatePingService {
    @GET("/api/iphone/")
    suspend fun getPing()
}

object VpnGateCsvApi{
    val retrofitService : VpnGateCsvService by lazy {
        retrofit.create(VpnGateCsvService::class.java)
    }
    val pingService: VpnGatePingService by lazy {
        retrofit.create(VpnGatePingService::class.java)
    }
    fun parseCsv(csvData: String): List<List<String>> {
        var a = csvData.drop(15)
        a = a.dropLast(3)
        return csvReader().readAll(a).drop(1)
    }
}
