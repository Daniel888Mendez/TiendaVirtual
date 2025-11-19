package com.dmo.tiendavirtual.Vendedor

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dmo.tiendavirtual.Constantes
import com.dmo.tiendavirtual.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityRegistroVendedorBinding
    private  lateinit var firebaseAuth: FirebaseAuth
    private  lateinit var  progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root) //acceso a todos los registros de activity_registro_vendedor.xml no nocesito usar findbyid

        firebaseAuth= FirebaseAuth.getInstance()
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnregistrarV.setOnClickListener {
            validarInfo()
        }


    }
    private var nombre=""
    private var email=""
    private var password=""
    private var cPassword=""
    private fun validarInfo() { //trim elimina espacios delante y detras
        nombre = binding.etNombreV.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        cPassword = binding.etCPassword.text.toString().trim()

        if (nombre.isEmpty()){
            binding.etNombreV.error="Ingrese nombre"
            binding.etNombreV.requestFocus() //Mantener puntero ebn el error
        }else if (email.isEmpty()){
            binding.etEmail.error="Ingrese email"
            binding.etEmail.requestFocus()
        }else if (password.isEmpty()){
            binding.etPassword.error="Ingrese password"
            binding.etPassword.requestFocus()
        }else if (cPassword.isEmpty()){
            binding.etCPassword.error="Ingrese password"
            binding.etCPassword.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.error="El email no es valido"
            binding.etEmail.requestFocus()
        }else if (password.length<=6){
            binding.etPassword.error="Necesitas 6 o mas caracteres"
            binding.etPassword.requestFocus()
        }else if (password!=cPassword){
            binding.etCPassword.error="Las contraseñas no coinciden"
            binding.etCPassword.requestFocus()
        }else{
            registrarVendedor() //si no salta error registro el vendedor
        }

    }

    private fun registrarVendedor() {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //si se efectua correctamente agrego a BD
                insertarInfoBD()

        }
            .addOnFailureListener {
                e->
                Toast.makeText(this,"Fallo en el registro.CAUSADO:${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInfoBD() {
        progressDialog.setMessage("Guardando Informacion en la BD")
        val uidBD=firebaseAuth.uid
        val nombreBD=nombre
        val emailBD=email
        val tipoUsuario="Vendedor"
        val tiempoBD= Constantes().obtenerTiempo()
        val fecha= Constantes().obtenerFecha(tiempoBD)

        val datosVendedor= HashMap<String, Any>()

        datosVendedor["uid"] = "${uidBD}"
        datosVendedor["nombreBD"] = "${nombreBD}"
        datosVendedor["emailBD"] = "${emailBD}"
        datosVendedor["tipoUsuario"] = "${tipoUsuario}"
        datosVendedor["tiempoBD"] = "${fecha}"

        val references= FirebaseDatabase.getInstance().getReference("Usuarios")
        references.child(uidBD!!)
            .setValue(datosVendedor)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityVendedor::class.java))
                finish()

        }
            .addOnFailureListener { e->
                Toast.makeText(this,"Fallo en la BD .CAUSA:${e.message}", Toast.LENGTH_SHORT).show()

            }

    }


}


