package com.example.teoguideas.Retrofit

import com.example.kotlincomicreader.Model.Banner
import com.example.teoguideas.Model.CentroHistorico
import io.reactivex.Observable
import retrofit2.http.GET


interface IComicAPI {
    @get:GET("banner")
    val bannerList: Observable<List<Banner>>

    @get:GET("centro")
    val CHistoList:Observable<List<CentroHistorico>>
}