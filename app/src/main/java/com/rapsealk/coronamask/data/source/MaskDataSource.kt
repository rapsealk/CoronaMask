package com.rapsealk.coronamask.data.source

import com.rapsealk.coronamask.data.model.Response
import com.rapsealk.coronamask.data.model.StoreSale
import io.reactivex.Single

/**
 * Created by rapsealk on 2020/03/11..
 */
interface MaskDataSource {

    fun getStoresByGeo(
        latitude: Double,
        longitude: Double,
        meters: Int = 5000
    ): Single<Response<StoreSale>>
}