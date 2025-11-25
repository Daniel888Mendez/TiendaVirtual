package com.dmo.tiendavirtual.Cliente.Botton_Nav_Fragments_Cliente

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.dmo.tiendavirtual.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class FragmentTiendaC : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var refUsuarios: DatabaseReference
    private lateinit var spinner: Spinner

    private val listaAsignaturas = mutableListOf("Todos") // Default

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tienda_c, container, false)

        spinner = view.findViewById(R.id.spinnerAsignaturas)
        refUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios")

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.mapTiendaFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val espana = LatLng(40.0, -3.7)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(espana, 6f))

        cargarAsignaturas()
        mostrarTodosLosUsuarios()

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                val seleccion = listaAsignaturas[position]
                if (seleccion == "Todos")
                    mostrarTodosLosUsuarios()
                else
                    filtrarPorAsignatura(seleccion)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun cargarAsignaturas() {
        refUsuarios.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val asignaturasSet = mutableSetOf<String>()

                for (usuarioSnap in snapshot.children) {
                    val asignatura = usuarioSnap.child("asignatura").value?.toString() ?: ""
                    if (asignatura.isNotEmpty())
                        asignaturasSet.add(asignatura)
                }

                listaAsignaturas.clear()
                listaAsignaturas.add("Todos")
                listaAsignaturas.addAll(asignaturasSet)

                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    listaAsignaturas
                )
                spinner.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun mostrarTodosLosUsuarios() {
        mMap.clear()

        refUsuarios.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (usuarioSnap in snapshot.children) {
                    val asignatura = usuarioSnap.child("asignatura").value?.toString() ?: ""
                    val ubicacion = usuarioSnap.child("ubicacion").value?.toString() ?: ""
                    val imagen = usuarioSnap.child("imagen").value?.toString() ?: ""

                    if (ubicacion.contains(",")) {
                        val (lat, lng) = ubicacion.split(",").map { it.toDouble() }
                        colocarMarcadorConImagen(lat, lng, imagen, asignatura)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun filtrarPorAsignatura(asig: String) {
        mMap.clear()

        refUsuarios.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (usuarioSnap in snapshot.children) {
                    val asignatura = usuarioSnap.child("asignatura").value?.toString() ?: ""

                    if (asignatura == asig) {
                        val ubicacion = usuarioSnap.child("ubicacion").value?.toString() ?: ""
                        val imagen = usuarioSnap.child("imagen").value?.toString() ?: ""

                        if (ubicacion.contains(",")) {
                            val (lat, lng) = ubicacion.split(",").map { it.toDouble() }
                            colocarMarcadorConImagen(lat, lng, imagen, asig)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /** --------------------------
     * MARCADOR CON IMAGEN DE BD
     * -------------------------- */
    private fun colocarMarcadorConImagen(lat: Double, lng: Double, urlImagen: String, titulo: String) {
        val pos = LatLng(lat, lng)

        val defaultImg = BitmapFactory.decodeResource(resources, R.drawable.img_perfil)

        Glide.with(requireContext())
            .asBitmap()
            .load(urlImagen)
            .error(defaultImg)
            .circleCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {

                    val icono = crearIconoCircularConBorde(bitmap)

                    mMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title(titulo)
                            .icon(BitmapDescriptorFactory.fromBitmap(icono))
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun crearIconoCircularConBorde(bitmap: Bitmap): Bitmap {
        val size = 150
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val rect = Rect(0, 0, size, size)
        val rectF = RectF(rect)

        // Imagen circular
        val path = Path()
        path.addOval(rectF, Path.Direction.CW)
        canvas.clipPath(path)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false)
        canvas.drawBitmap(scaledBitmap, null, rectF, paint)

        // Borde blanco
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f
        canvas.drawOval(rectF, paint)

        return output
    }
}
