package com.dmo.tiendavirtual.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.dmo.tiendavirtual.SeleccionarTipoActivity
import com.google.firebase.auth.FirebaseAuth

object  ToastUtils{
    fun showToast(context: Context, text: String, duration: Int){
        Toast.makeText(context, text, duration).show()
    }
}
object SesionUtil {

    fun comprobarSesion(context: Context, firebaseAuth: FirebaseAuth) {
        firebaseAuth.currentUser?.let {
            Toast.makeText(context, "Usuario en línea prueba", Toast.LENGTH_SHORT).show()
        } ?: run {
            val intent = Intent(context, SeleccionarTipoActivity::class.java)
            context.startActivity(intent)
            if (context is Activity) {
                context.finishAffinity()
            }
        }
    }

    fun cerrarSesion(context: Context, firebaseAuth: FirebaseAuth) {
        firebaseAuth.signOut()
        val intent = Intent(context, SeleccionarTipoActivity::class.java)
        context.startActivity(intent)
        if (context is Activity) {
            context.finishAffinity()
        }
        Toast.makeText(context, "Cerraste sesión", Toast.LENGTH_SHORT).show()
    }
}
