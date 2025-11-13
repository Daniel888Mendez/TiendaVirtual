package com.dmo.tiendavirtual

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dmo.tiendavirtual.Cliente.LoginClienteActivity
import com.dmo.tiendavirtual.Cliente.MainActivityCliente
import com.dmo.tiendavirtual.Vendedor.LoginVendedorActivity
import com.dmo.tiendavirtual.Vendedor.MainActivityVendedor
import com.dmo.tiendavirtual.databinding.ActivitySeleccionarTipoBinding

class SeleccionarTipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionarTipoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tipoVendedor.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginVendedorActivity::class.java))
            finishAffinity()
        }

        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoActivity, LoginClienteActivity::class.java))
            finishAffinity()
        }

    }
}