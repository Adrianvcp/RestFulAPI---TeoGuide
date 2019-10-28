package com.example.teoguideas.Retrofit

import com.example.teoguideas.Model.CentroHistorico
import io.reactivex.Observable
import retrofit2.http.GET


interface IComicAPI {
    @get:GET("centro")
    val CHistoList:Observable<List<CentroHistorico>>

    @get:GET("buscarFoto")
    val PerfilList:Observable<List<CentroHistorico>>
}