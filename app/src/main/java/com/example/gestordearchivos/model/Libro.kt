package com.example.gestordearchivos.model

import java.io.Serializable

data class Libro(
    val id: Int = 0,
    val titulo: String,
    val autor: String,
    val genero: String,
    val sinopsis: String,
    val rutaPdf: String,
    val rutaPortada: String,
    val soloAdultos: Boolean = false,
    val progreso: Int = 0
) : Serializable
