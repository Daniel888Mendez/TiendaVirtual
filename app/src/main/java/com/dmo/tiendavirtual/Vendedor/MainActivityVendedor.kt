package com.dmo.tiendavirtual.Vendedor

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.dmo.tiendavirtual.R
import com.dmo.tiendavirtual.Vendedor.Boton_Nav_Fragmrento.FragmentMisProductos
import com.dmo.tiendavirtual.Vendedor.Boton_Nav_Fragmrento.FragmentOrdenesV
import com.dmo.tiendavirtual.Vendedor.nav_fragment_vendedor.FragmentInicioV
import com.dmo.tiendavirtual.Vendedor.nav_fragment_vendedor.FragmentMiTiendaV
import com.dmo.tiendavirtual.Vendedor.nav_fragment_vendedor.FragmentResenia
import com.dmo.tiendavirtual.databinding.ActivityMainVendedorBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityVendedor : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainVendedorBinding
    private  var firebaseAuth : FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtener Vistas
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        firebaseAuth= FirebaseAuth.getInstance()
        comprobarSesion()

        binding.navigationView.setNavigationItemSelectedListener(this)

        //configurar toggle del drawer
        val toggle =
            ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
            )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.icicio_menu)
    }


    private  fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, LoginVendedorActivity::class.java))
        finish()
        Toast.makeText(applicationContext,"Has cerrado la sesion", Toast.LENGTH_SHORT).show()
    }
    private fun comprobarSesion() {
        //Si el usuario no ha iniciado sesion lo manda a la activiti de registro
        if (firebaseAuth!!.currentUser==null){
            startActivity(Intent(applicationContext, LoginVendedorActivity::class.java))
            Toast.makeText(applicationContext,"Vendedor no registrado o logueado", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext,"Vendedor en linea", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment,fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.icicio_menu->{
                replaceFragment(FragmentInicioV())
            }
            R.id.mitienda_menu->{
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.reseñas_menu->{
                replaceFragment(FragmentResenia())
            }
            R.id.cerrar_menu->{
                cerrarSesion()
            }
            R.id.menu_misProductos->{
                replaceFragment(FragmentMisProductos())
            }
            R.id.boton_ordenes->{
                replaceFragment(FragmentOrdenesV())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}