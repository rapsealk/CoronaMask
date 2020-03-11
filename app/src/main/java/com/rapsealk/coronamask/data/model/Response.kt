package com.rapsealk.coronamask.data.model

/**
 * Created by rapsealk on 2020/03/11..
 */
data class Response<T>(
    val count: Int,
    val stores: List<T>
)