package com.rapsealk.coronamask.data.source.remote

import com.rapsealk.coronamask.data.model.Response
import com.rapsealk.coronamask.data.model.StoreSale
import com.rapsealk.coronamask.data.source.MaskDataSource
import io.reactivex.Single

/**
 * Created by rapsealk on 2020/03/11..
 */
class MaskRemoteDataSource(
    private val maskService: PublicMaskService = PublicMaskService.create()
) : MaskDataSource {

    override fun getStoresByGeo(
        latitude: Double,
        longitude: Double,
        meters: Int
    ): Single<Response<StoreSale>> {
        return maskService.getStoresByGeo(latitude, longitude, meters)
    }
}