package com.rapsealk.coronamask.data.model

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng

/**
 * Created by rapsealk on 2020/03/11..
 */
data class StoreSale(
    // TODO: SerializedName
    val code: String,           // 식별 코드
    val name: String,           // 이름
    val addr: String,           // 주소
    val type: String,           // 판매처 유형 [약국: '01', 우체국: '02', 농협: '03']
    val lat: Float,             // 위도
    val lng: Float,             // 경도
    val stock_at: String,       // 입고시간 (YYYY/MM/DD HH:mm:ss)
    val remain_stat: String?,   // 재고 상태 ['plenty' - 100개 이상 (녹색)
                                //          'some' - 30개 이상 100개 미만 (노란색)
                                //          'few' - 2개 이상 30개 미만 (빨강색)
                                //          'empty' - 1개 이하 (회색)
    val created_at: String      // 데이터 생성 일자 (YYYY/MM/DD HH:mm:ss)
) {
    fun getLatLng(): LatLng = LatLng(lat.toDouble(), lng.toDouble())

    fun getRemainStat(): String = when (remain_stat) {
        "plenty" -> "100개 이상"
        "some" -> "30개 이상 100개 미만"
        "few" -> "2개 이상 30개 미만"
        else -> "없음"
    }

    fun getColor(): Float = when (remain_stat) {
        "plenty" -> BitmapDescriptorFactory.HUE_GREEN
        "some" -> BitmapDescriptorFactory.HUE_YELLOW
        "few" -> BitmapDescriptorFactory.HUE_RED
        else -> BitmapDescriptorFactory.HUE_BLUE
    }
}