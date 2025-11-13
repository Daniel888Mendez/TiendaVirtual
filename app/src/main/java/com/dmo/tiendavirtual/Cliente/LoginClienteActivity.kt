package com.dmo.tiendavirtual.Cliente

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dmo.tiendavirtual.databinding.ActivityLoginClienteBinding
import com.dmo.tiendavirtual.databinding.ActivitySeleccionarTipoBinding

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(this@LoginClienteActivity, RegistroClienteActivity::class.java))
        }



    }
}