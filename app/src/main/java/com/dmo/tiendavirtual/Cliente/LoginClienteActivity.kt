package com.dmo.tiendavirtual.Cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dmo.tiendavirtual.databinding.ActivityLoginClienteBinding
import com.google.firebase.auth.FirebaseAuth

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnLoginC.setOnClickListener {
            validarInfo()
        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(this@LoginClienteActivity, RegistroClienteActivity::class.java))
        }
    }

    private var email=""
    private var password=""

    private fun validarInfo() {
        email=binding.etEmailC.text.toString().trim()
        password=binding.etPasswordC.text.toString().trim()

        if (email.isEmpty()){
            binding.etEmailC.error="Ingrese Email"
            binding.etEmailC.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmailC.error="Email no valido"
            binding.etEmailC.requestFocus()
        }else if (password.isEmpty()){
            binding.etPasswordC.error="Ingrese Password"
            binding.etPasswordC.requestFocus()
        }else{
            loginCliente()
        }

    }

    private fun loginCliente() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
                Toast.makeText(this,"Bienvenido", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"No se puede iniciar sesion . CAUSA: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}