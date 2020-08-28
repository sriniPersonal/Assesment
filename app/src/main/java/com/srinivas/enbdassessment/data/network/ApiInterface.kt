package com.srinivas.enbdassessment.data.network


import com.srinivas.enbdassessment.data.network.models.PixabayResponse
import com.srinivas.enbdassessment.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/api/")
  suspend  fun getImageResults(
        @Query("key") key: String?,
        @Query("q") query: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<PixabayResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): ApiInterface {

            val interceptor =
                Class.forName("com.facebook.stetho.okhttp3.StethoInterceptor")
                    .newInstance()
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addNetworkInterceptor(interceptor as Interceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}