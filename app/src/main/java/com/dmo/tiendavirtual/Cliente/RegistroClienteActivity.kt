package com.dmo.tiendavirtual.Cliente

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dmo.tiendavirtual.Constantes
import com.dmo.tiendavirtual.common.ToastUtils
import com.dmo.tiendavirtual.databinding.ActivityRegistroClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroClienteBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnregistrarC.setOnClickListener {
          //  Toast.makeText(this, "prueba", Toast.LENGTH_SHORT).show()
            validarInformacion()
        }

    }

    private var nombre = ""
    private var email = ""
    private var password = ""
    private var cpassword = ""
    private fun validarInformacion() {
        nombre = binding.etNombreC.text.toString().trim()
        email = binding.etEmailC.text.toString().trim()
        password = binding.etPasswordC.text.toString().trim()
        cpassword = binding.etCPasswordC.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.etNombreC.error = "Ingrese nombres"
            binding.etNombreC.requestFocus()
        } else if (email.isEmpty()) {
            binding.etEmailC.error = "Engrese email"
            binding.etEmailC.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailC.error = "Email no valido"
            binding.etEmailC.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPasswordC.error = "Ingrese Password"
            binding.etPasswordC.requestFocus()
        } else if (password.length < 6) {
            binding.etPasswordC.error = "Necesita mas de 6 caracteres"
            binding.etPasswordC.requestFocus()
        } else if (password != cpassword) {
            binding.etCPasswordC.error = "La contraseña tiene que coincidir"
            binding.etCPasswordC.requestFocus()
        } else {
            registrarCliente()
        }

    }

    private fun registrarCliente() {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                insertarInfoBD()
            }
            .addOnFailureListener { e ->
                ToastUtils.showToast(this,"Fallo en el registro CAUSA: ${e.message}", Toast.LENGTH_SHORT)
                progressDialog.hide()
            }
    }

    private fun insertarInfoBD() {
        progressDialog.setMessage("Guardando Informacion")
        val uid = firebaseAuth.uid
        val nombreC = nombre
        val emailC = email
        val tiempoRegistro = Constantes().obtenerTiempo()
        val fecha= Constantes().obtenerFecha(tiempoRegistro)

        val datosClientes = HashMap<String, Any>()

        datosClientes["uid"] = "$uid"
        datosClientes["nombre"] = "$nombreC"
        datosClientes["email"] = "$emailC"
        datosClientes["telefono"]
        datosClientes["provedor"]= "email"
        datosClientes["fechaRegistro"] = "Fecha de Registro: $fecha"
        datosClientes["imagen"] = ""
        datosClientes["tipoUsuario"] = "Cliente"

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid!!)
            .setValue(datosClientes)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this@RegistroClienteActivity, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo en el registro CAUSA: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

}





