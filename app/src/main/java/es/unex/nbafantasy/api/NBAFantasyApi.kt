package es.unex.nbafantasy.api

import com.example.example.SeasonAverages
import com.example.example.SeasonData
import es.unex.nbafantasy.Data.api.Data
import es.unex.nbafantasy.Data.api.NBAPlayers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val service: NBAFantasyApi by lazy {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    val retrofit = Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl("https://www.balldontlie.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    retrofit.create(NBAFantasyApi::class.java)
}
fun getNetworkService() = service

class APIError(message: String, cause: Throwable?) : Throwable(message, cause)

interface NBAFantasyApi {
    @GET("players")
    suspend fun getPlayers(): NBAPlayers

    @GET("players/{id}")
    suspend  fun getPlayerById(@Path("id") playerId: Int): Data
    @GET("season_averages")
    suspend fun getSeasonAverage(
        @Query("player_ids[]") playerNumberIds: Int): SeasonAverages
}


