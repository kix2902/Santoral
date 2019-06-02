package es.kix2902.santoral.data

import Model
import es.kix2902.santoral.SANTOPEDIA_API_APP_ID
import es.kix2902.santoral.SANTOPEDIA_API_APP_KEY
import es.kix2902.santoral.SANTOPEDIA_API_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SantopediaApi {

    companion object {
        fun create(): SantopediaApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SANTOPEDIA_API_URL)
                .build()

            return retrofit.create(SantopediaApi::class.java)
        }
    }

    @GET("days/{month}-{date}?app_id=$SANTOPEDIA_API_APP_ID&app_key=$SANTOPEDIA_API_APP_KEY")
    fun getDay(@Path("month") month: Int, @Path("date") date: Int, @Query("locale") locale: String): Call<List<Model.ApiResponse>>

    @GET("names/{name}?app_id=$SANTOPEDIA_API_APP_ID&app_key=$SANTOPEDIA_API_APP_KEY")
    fun getName(@Path("name") name: String, @Query("locale") locale: String): Call<List<Model.ApiResponse>>

}