package com.dmo.tiendavirtual.Cliente.Nav_Fragments_Cliente

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.dmo.tiendavirtual.databinding.FragmentMiPerfilCBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class FragmentMiPerfilC : Fragment() {
    private lateinit var binding: FragmentMiPerfilCBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog
    private var imagenUri: Uri? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMiPerfilCBinding.inflate(inflater, container, false)
        binding.btnActualizar.setOnClickListener {
            actualizarPerfil()
        }
        // Nuevo: botón para elegir imagen
        binding.profileImageView.setOnClickListener {
            seleccionarImg()
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()
        leerInformacion()

    }

    private fun leerInformacion() {

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombre = snapshot.child("nombre").value?.toString() ?: ""
                    val email = snapshot.child("email").value?.toString() ?: ""
                    val dni = snapshot.child("dni").value?.toString() ?: ""
                    val imagen = snapshot.child("imagen").value?.toString() ?: ""
                    val telefono = snapshot.child("telefono").value?.toString() ?: ""
                    val fecha = snapshot.child("fechaRegistro").value?.toString() ?: ""

                    binding.nombreCPerfil.setText(nombre)
                    binding.emailCPerfil.setText(email)
                    binding.dniCPerfil.setText(dni)
                    binding.telefonoCPerfil.setText(telefono)
                    binding.fechaRegistroCPerfil.setText(fecha)

                    // Mostrar imagen si existe URL
                    if (imagen.isNotEmpty()) {
                        com.bumptech.glide.Glide.with(mContext)
                            .load(imagen)
                            .into(binding.profileImageView)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(mContext, "Error al leer datos", Toast.LENGTH_SHORT).show()
                }
            })

    }

    private fun seleccionarImg() {

        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
            resultadoImg.launch(intent)}
    }

    private val resultadoImg =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val data = resultado.data
                imagenUri = data?.data
                binding.profileImageView.setImageURI(imagenUri) // mostrar seleccionada
                subirImagenStorage(imagenUri)
            } else {
                Toast.makeText(mContext, "Acción cancelada", Toast.LENGTH_SHORT).show()
            }
        }

    private fun subirImagenStorage(imagenUri: Uri?) {
        if (imagenUri == null) return
        progressDialog.setMessage("Subiendo imagen...")
        progressDialog.show()
        val rutaImagen = "imagenesPerfil/${firebaseAuth.uid}.jpg"
        val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
        storageRef.putFile(imagenUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val urlImagen = uri.toString()
                    // Guardar URL en la DB de usuario

                    val refUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios")
                    refUsuarios.child(firebaseAuth.uid!!).child("imagen").setValue(urlImagen)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(
                                mContext,
                                "Imagen actualizada correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(mContext, "Error al guardar URL", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }

            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(mContext, "Error al subir imagen", Toast.LENGTH_SHORT).show()
            }

    }

    private fun actualizarPerfil() {

        progressDialog.setMessage("Actualizando Perfil")
        progressDialog.show()
        val uid = firebaseAuth.uid!!
        val nombreC = binding.nombreCPerfil.text.toString()
        val emailC = firebaseAuth.currentUser?.email ?: ""
        val dniC = binding.dniCPerfil.text.toString()
        val telefonoC = binding.telefonoCPerfil.text.toString()
        val fecha = binding.fechaRegistroCPerfil.text.toString()
        val datosClientes = HashMap<String, Any>()
        datosClientes["uid"] = uid
        datosClientes["nombre"] = nombreC
        datosClientes["email"] = emailC
        datosClientes["provedor"] = "email"
        datosClientes["dni"] = dniC
        datosClientes["telefono"] = telefonoC
        datosClientes["fechaRegistro"] = fecha
        datosClientes["tipoUsuario"] = "Cliente"
        FirebaseDatabase.getInstance().getReference("Usuarios")
            .child(uid)
            .updateChildren(datosClientes)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
    }

}
