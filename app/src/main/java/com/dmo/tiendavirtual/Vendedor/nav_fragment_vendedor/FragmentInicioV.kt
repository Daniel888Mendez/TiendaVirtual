package com.dmo.tiendavirtual.Vendedor.nav_fragment_vendedor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dmo.tiendavirtual.R
import com.dmo.tiendavirtual.Vendedor.Boton_Nav_Fragmrento.FragmentMisProductos
import com.dmo.tiendavirtual.Vendedor.Boton_Nav_Fragmrento.FragmentOrdenesV
import com.dmo.tiendavirtual.databinding.FragmentInicioVBinding

class FragmentInicioV : Fragment() {
    private lateinit var binding: FragmentInicioVBinding
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        mContext=context
        super.onAttach(context)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentInicioVBinding.inflate(inflater,container,false)
        binding.botonNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_misProductos->{
                    replaceFragment(FragmentMisProductos())

                }
                R.id.boton_ordenes->{
                    replaceFragment(FragmentOrdenesV())

                }
            }
            true
        }
        replaceFragment(FragmentMisProductos())
        binding.botonNavigation.selectedItemId=R.id.menu_misProductos
        binding.addFab.setOnClickListener {
            Toast.makeText(mContext,"Has presionado en el boton Flotante", Toast.LENGTH_SHORT).show()
        }
        return binding.root
       // return inflater.inflate(R.layout.fragment_inicio_v, container, false)
    }
    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.botonFragmento,fragment)
            .commit()


    }
}