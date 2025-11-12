package com.dmo.tiendavirtual.Vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dmo.tiendavirtual.R
import com.dmo.tiendavirtual.databinding.ActivityLoginVendedorBinding
import com.dmo.tiendavirtual.databinding.ActivityMainVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import org.intellij.lang.annotations.Pattern

class LoginVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginVendedorBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var  progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false) //no se oculta cuando presiona fuera

        binding.btnLogin.setOnClickListener {
            validarInfo()
        }
        binding.tvRegistrarV.setOnClickListener {
            startActivity(Intent(this, RegistroVendedorActivity::class.java))
        }

    }

    private var email=""
    private var password=""
    private fun validarInfo() {
        email=binding.etEmail.text.toString().trim()
        password=binding.etPassword.text.toString().trim()
        if (email.isEmpty()){
            binding.etEmail.error="Ingrese Email"
            binding.etEmail.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error="Email no valido"
            binding.etEmail.requestFocus()
        }else if (password.isEmpty()){
            binding.etPassword.error="Ingrese Password"
            binding.etPassword.requestFocus()
        }else{
            loginVendedor()
        }

    }

    private fun loginVendedor() {
        progressDialog.setMessage("Ingresando")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityVendedor::class.java))
                finishAffinity()
                Toast.makeText(this,"Bienvenido", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,"No se puede iniciar sesion . CAUSA: ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }
}