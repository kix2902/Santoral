package es.kix2902.santoral.data

import es.kix2902.santoral.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SantopediaApi {

    companion object {
        const val SANTOPEDIA_API_URL = "https://api.santoral.app/"

        fun create(): SantopediaApi {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
            }
            val client = OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SANTOPEDIA_API_URL)
                .client(client)
                .build()

            return retrofit.create(SantopediaApi::class.java)
        }
    }

    @GET("api.php")
    fun getDate(@Query("date") date: String): Call<List<Model.Saint>>

    @GET("api.php")
    fun getName(@Query("name") name: String): Call<List<Model.Saint>>

}