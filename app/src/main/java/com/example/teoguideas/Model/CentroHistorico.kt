package com.example.teoguideas.Model

class CentroHistorico {

    var idCentroH:Int?=null
    var nNombre:String?=null
    var tUbicacion:String?=null
    var dHistoria:String?=null
    var NCorx:Int?=null
    var NCory:Int?=null
    var imgportada:String?=null
    
    constructor(){}


    constructor(
        idCentroH: Int?,
        nNombre: String?,
        tUbicacion: String?,
        dHistoria: String?,
        NCorx: Int?,
        NCory: Int?,
        imgportada: String?
    ) {
        this.idCentroH = idCentroH
        this.nNombre = nNombre
        this.tUbicacion = tUbicacion
        this.dHistoria = dHistoria
        this.NCorx = NCorx
        this.NCory = NCory
        this.imgportada = imgportada
    }
}