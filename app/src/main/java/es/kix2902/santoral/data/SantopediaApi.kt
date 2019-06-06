package es.kix2902.santoral.data

import Model
import es.kix2902.santoral.BuildConfig
import es.kix2902.santoral.SANTOPEDIA_API_APP_ID
import es.kix2902.santoral.SANTOPEDIA_API_APP_KEY
import es.kix2902.santoral.SANTOPEDIA_API_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SantopediaApi {

    companion object {
        fun create(): SantopediaApi {
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                interceptor.level = HttpLoggingInterceptor.Level.NONE
            }
            val client = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SANTOPEDIA_API_URL)
                .client(client)
                .build()

            return retrofit.create(SantopediaApi::class.java)
        }
    }

    @GET("days/{month}-{date}?app_id=$SANTOPEDIA_API_APP_ID&app_key=$SANTOPEDIA_API_APP_KEY")
    fun getDay(@Path("month") month: String, @Path("date") date: String, @Query("locale") locale: String): Call<List<Model.ApiResponse>>

    @GET("names/{name}?app_id=$SANTOPEDIA_API_APP_ID&app_key=$SANTOPEDIA_API_APP_KEY")
    fun getName(@Path("name") name: String, @Query("locale") locale: String): Call<List<Model.ApiResponse>>

}