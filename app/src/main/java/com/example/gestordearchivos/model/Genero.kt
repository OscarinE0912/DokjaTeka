package com.example.gestordearchivos.model

enum class Genero(val nombreMostrable: String) {
    FANTASIA("Fantasía"),
    CIENCIA_FICCION("Ciencia Ficción"),
    TERROR("Terror"),
    ROMANCE("Romance"),
    EDUCATIVO("Educativo"),
    HISTORIA("Historia"),
    MISTERIO("Misterio"),
    COMEDIA("Comedia"),
    DRAMA("Drama");

    companion object {
        fun desdeNombre(nombre: String): Genero? {
            return entries.find { it.name == nombre.trim() }
        }
    }
}
