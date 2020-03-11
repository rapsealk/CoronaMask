package com.rapsealk.coronamask.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.rapsealk.coronamask.data.model.StoreSale
import com.rapsealk.coronamask.data.source.MaskRepository
import com.rapsealk.coronamask.data.source.remote.MaskRemoteDataSource
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by rapsealk on 2020/03/11..
 */
class MainViewModel(
    private val maskRepository: MaskRepository = MaskRepository(MaskRemoteDataSource())
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _stores = MutableLiveData<List<StoreSale>>()
    val stores: LiveData<List<StoreSale>> = _stores

    private val _progressVisibility = MutableLiveData<Boolean>(false)
    val progressVisibility: LiveData<Boolean> = _progressVisibility

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    fun setCurrentLocation(location: LatLng) {
        _progressVisibility.value = true
        if (compositeDisposable.size() > 0) {
            compositeDisposable.clear()
        }
        compositeDisposable.add(
            maskRepository.getStoresByGeo(location.latitude, location.longitude)
                .subscribe({ response ->
                    Log.d(TAG, "StoresByGeo: ${response.count} stores found.")
                    _stores.value = response.stores
                    _progressVisibility.value = false
                }, { exception ->
                    Log.e(TAG, "Failed to get stores.", exception)
                    _progressVisibility.value = false
                })
        )
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
}