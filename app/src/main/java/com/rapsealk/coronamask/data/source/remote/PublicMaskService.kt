package com.rapsealk.coronamask.data.source.remote

import com.rapsealk.coronamask.data.model.Response
import com.rapsealk.coronamask.data.model.StoreSale
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by rapsealk on 2020/03/11..
 */
interface PublicMaskService {

    /**
     * https://app.swaggerhub.com/apis-docs/Promptech/public-mask-info/20200307-oas3#/
     */

    @GET("storesByGeo/json")
    fun getStoresByGeo(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("m") meters: Int = 3000
    ): Single<Response<StoreSale>>

    companion object {
        private val TAG = PublicMaskService::class.java.simpleName

        fun create(): PublicMaskService = Retrofit.Builder()
            .baseUrl("https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PublicMaskService::class.java)
    }
}