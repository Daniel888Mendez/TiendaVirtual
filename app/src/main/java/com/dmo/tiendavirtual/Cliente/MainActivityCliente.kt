package com.dmo.tiendavirtual.Cliente

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.dmo.tiendavirtual.Cliente.Botton_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.dmo.tiendavirtual.Cliente.Botton_Nav_Fragments_Cliente.FragmentTiendaC
import com.dmo.tiendavirtual.Cliente.Nav_Fragments_Cliente.FragmentInicioC
import com.dmo.tiendavirtual.Cliente.Nav_Fragments_Cliente.FragmentMiPerfilC


import com.dmo.tiendavirtual.R
import com.dmo.tiendavirtual.common.SesionUtil
import com.dmo.tiendavirtual.databinding.ActivityMainClienteBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityCliente : AppCompatActivity()  , NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainClienteBinding
    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        firebaseAuth= FirebaseAuth.getInstance()
        comprobarSesion()

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toggle= ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        replaceFragment(FragmentInicioC())

    }
    private fun comprobarSesion(){
        SesionUtil.comprobarSesion(this@MainActivityCliente,firebaseAuth)
        /*

        firebaseAuth.currentUser?.let { user ->
            Toast.makeText(this,"Usuario en lilnea prueba ",Toast.LENGTH_SHORT).show()
        }?:{
            startActivity(Intent(this@MainActivityCliente, SeleccionarTipoActivity::class.java))
            finishAffinity()
        }

         */

        /*
        if (firebaseAuth.currentUser==null){
            startActivity(Intent(this@MainActivityCliente, SeleccionarTipoActivity::class.java))
            finishAffinity()
        }else{
            Toast.makeText(this,"Usuario en lilnea",Toast.LENGTH_SHORT).show()
        }

         */
    }
    private fun  cerrarSesion(){

        SesionUtil.cerrarSesion(this,firebaseAuth)
        /*
        firebaseAuth.signOut()
        startActivity(Intent(this@MainActivityCliente, SeleccionarTipoActivity::class.java))
        finishAffinity()
        Toast.makeText(this,"Cerraste sesion",Toast.LENGTH_SHORT).show()

         */
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.navFragment,fragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.op_inicio_c->{
                replaceFragment(FragmentInicioC())
            }
            R.id.op_mi_perfil_c->{
                replaceFragment(FragmentMiPerfilC())
            }
            R.id.op_cerrer_sesion_c->{
                cerrarSesion()
            }
            R.id.op_tienda_c->{
                replaceFragment(FragmentTiendaC())
            }
            R.id.op_mi_ordenes_c->{
                replaceFragment(FragmentMisOrdenesC())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }
}