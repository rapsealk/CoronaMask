package com.rapsealk.coronamask.data.source

import com.rapsealk.coronamask.data.model.Response
import com.rapsealk.coronamask.data.model.StoreSale
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by rapsealk on 2020/03/11..
 */
class MaskRepository(
    private val maskRemoteDataSource: MaskDataSource
) : MaskDataSource {

    override fun getStoresByGeo(
        latitude: Double,
        longitude: Double,
        meters: Int
    ): Single<Response<StoreSale>> {
        return maskRemoteDataSource.getStoresByGeo(latitude, longitude, meters)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}