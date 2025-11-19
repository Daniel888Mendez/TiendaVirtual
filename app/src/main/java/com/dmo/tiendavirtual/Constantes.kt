package com.dmo.tiendavirtual

import android.text.format.DateFormat
import java.util.Calendar
import java.util.Locale


class Constantes {
    fun obtenerTiempo(): Long{
        return System.currentTimeMillis()
    }

    //funcion para pasar la fecha de la BD que viene en tipo Long a string
    fun obtenerFecha(tiempo: Long?): String{
        if (tiempo==null){
            return "Fecha no disponible"
        }
        val calendar= Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis=tiempo
        return DateFormat.format("dd/MM/yyyy",calendar).toString()
    }
}