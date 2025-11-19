package com.dmo.tiendavirtual.Cliente.Nav_Fragments_Cliente

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dmo.tiendavirtual.databinding.FragmentMiPerfilCBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentMiPerfilC : Fragment() {
    private lateinit var binding: FragmentMiPerfilCBinding
    private  lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog
    private var imagenUri: Uri?=null //Puede ser valor nulo, gracias al simbolo? hacemos uso del null safety

    //Para poder inicializar el contexto
    override fun onAttach(context : Context) {
        super.onAttach(context)
        mContext=context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentMiPerfilCBinding.inflate(layoutInflater,container,false)
        binding.btnActualizar.setOnClickListener {
            actualizarPerfil()
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth= FirebaseAuth.getInstance() //Inicializar Firebase
        leerInformacio()
    }


    private var nombre = ""
    private var email = ""
    private var dni=""
    private var telefono=""
    private var ubicacion=""
    private var fechaRegistro=""


    private fun leerInformacio() {
        var ref= FirebaseDatabase.getInstance().getReference("Usuarios")
        //leer usuario actual atraves de uid. con firebaseAuth es la persona que ha iniciado sesion
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Obtener los datos del usuario
                    val nombre="${snapshot.child("nombre").value?.toString() ?:""}"
                    val email="${snapshot.child("email").value?.toString() ?:""}"
                    val dni="${snapshot.child("dni").value?.toString() ?:""}"
                    val imagen = "${snapshot.child("imagen").value}"
                    val telefono="${snapshot.child("telefono").value?.toString() ?:""}"
                    val fecha="${snapshot.child("fechaRegistro").value?.toString() ?:""}"

                    binding.nombreCPerfil.setText(nombre)
                    binding.emailCPerfil.setText(email)
                    binding.dniCPerfil.setText(dni)
                    binding.telefonoCPerfil.setText(telefono)
                    binding.fechaRegistroCPerfil.setText(fecha)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun seleccionarImg(){
        ImagePicker.with(this)
            .crop()//recortar img
            .compress(1024)//compresion -1megabyte
            .maxResultSize(1080,1080)
            .createIntent { intent ->

            }
    }

    private val resultadoImg=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
        if (resultado.resultCode== Activity.RESULT_OK){ //si se ha obtenido la imagen de manera exitosa
            val data=resultado.data
            imagenUri=data!!.data
            subirImagenStorage(imagenUri)

        }else{
            Toast.makeText(mContext,"Accion cancelada", Toast.LENGTH_SHORT).show()
        }

    }

    private fun subirImagenStorage(imagenUri: Uri?) {
        val rutaImagen="imagenesPerfil/"+firebaseAuth.uid  //nombre de la carpeta en el storage y nombre de la imagen el uid del usuarion
        //TERMINAR EL VIDEO 94
    }

    private fun actualizarPerfil() {

        progressDialog.setMessage("Actualizando Perfil")
        val uid = firebaseAuth.uid
        val nombreC = binding.nombreCPerfil.text.toString()
        val emailC = firebaseAuth.currentUser?.email
        val dniC=binding.dniCPerfil.text.toString()
        val telefonoC=binding.telefonoCPerfil.text
        val fecha=binding.fechaRegistroCPerfil.text.toString()
        val ubicacionC=ubicacion

        val datosClientes = HashMap<String, Any>()
        datosClientes["uid"] = "$uid"
        datosClientes["nombre"] = "$nombreC"
        datosClientes["email"] = "$emailC"
        datosClientes["provedor"]= "email"
        datosClientes["dni"]="$dniC"
        datosClientes["telefono"]="$telefonoC"
        datosClientes["ubicacion"]="$ubicacionC"
        datosClientes["fechaRegistro"] = "$fecha"
        datosClientes["imagen"] = ""
        datosClientes["tipoUsuario"] = "Cliente"

        val reference = FirebaseDatabase.getInstance().getReference("Usuarios")
        reference.child(uid!!)
            .setValue(datosClientes)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(context, "SUpuestamente Actualizado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(context, "SUpuestamente ERROR", Toast.LENGTH_SHORT).show()

            }


    }






}