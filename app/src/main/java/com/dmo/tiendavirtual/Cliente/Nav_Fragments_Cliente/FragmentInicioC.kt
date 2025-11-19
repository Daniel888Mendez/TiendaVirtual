package com.dmo.tiendavirtual.Cliente.Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmo.tiendavirtual.Cliente.Botton_Nav_Fragments_Cliente.FragmentMisOrdenesC
import com.dmo.tiendavirtual.Cliente.Botton_Nav_Fragments_Cliente.FragmentTiendaC
import com.dmo.tiendavirtual.R
import com.dmo.tiendavirtual.databinding.FragmentInicioCBinding

class FragmentInicioC : Fragment() {

    private lateinit var binding: FragmentInicioCBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentInicioCBinding.inflate(inflater,container,false)

        binding.bottonNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_tienda_c->{
                    replaceFragment(FragmentTiendaC())

                }
                R.id.op_mi_ordenes_c->{
                    replaceFragment(FragmentMisOrdenesC())
                }
            }
            true
        }
        replaceFragment(FragmentTiendaC())
        binding.bottonNavigation.selectedItemId=R.id.op_tienda_c
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.bottonFragment,fragment).commit()
    }

}