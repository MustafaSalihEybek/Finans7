package com.finans7.view.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.finans7.R
import com.finans7.databinding.FragmentContactBinding

class ContactFragment : Fragment() {
    private lateinit var v: View
    private lateinit var contactBinding: FragmentContactBinding

    private fun init(){
        contactBinding.contactFragmentImgBack.setOnClickListener {
            Navigation.findNavController(v).popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        contactBinding = FragmentContactBinding.inflate(inflater)
        return contactBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }
}