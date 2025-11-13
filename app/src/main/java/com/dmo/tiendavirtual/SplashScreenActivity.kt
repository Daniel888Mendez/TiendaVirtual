package com.dmo.tiendavirtual

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.dmo.tiendavirtual.Cliente.MainActivityCliente
import com.dmo.tiendavirtual.Vendedor.MainActivityVendedor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashScreenActivity : AppCompatActivity() {

    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        firebaseAuth= FirebaseAuth.getInstance()

        verBienvenida()

    }

    private fun verBienvenida() {
        object : CountDownTimer(3000,1000){
            override fun onFinish() {
                comprobarTipoUsuario()
            }

            override fun onTick(millisUntilFinished: Long) {
            }

        }.start()
    }

    private fun comprobarTipoUsuario(){
        val usuario=firebaseAuth.currentUser
        if (usuario==null){
            startActivity(Intent(this, MainActivityVendedor::class.java))
        }else{
            val reference= FirebaseDatabase.getInstance().getReference("Usuarios")
            reference.child(usuario.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val tipoU=snapshot.child("tipoUsuario").value
                        if (tipoU=="Vendedor"){
                            startActivity(Intent(this@SplashScreenActivity, MainActivityVendedor::class.java))
                            finishAffinity()
                        }else if (tipoU=="Cliente"){
                            startActivity(Intent(this@SplashScreenActivity, MainActivityCliente::class.java))
                            finishAffinity()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
    }
}