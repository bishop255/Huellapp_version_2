package com.example.huellapp.session

import com.example.huellapp.model.Usuario

object UserSession {
    var usuarioLogueado: Usuario? = null

    fun cerrarSesion() {
        usuarioLogueado = null
    }
}
