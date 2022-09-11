package com.finans7.view.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.finans7.R
import com.finans7.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private lateinit var settingsBinding: FragmentSettingsBinding
    private lateinit var navDirections: NavDirections

    private fun init(){
        settingsBinding.settingsFragmentRelativeSetting1.setOnClickListener(this)
        settingsBinding.settingsFragmentRelativeSetting2.setOnClickListener(this)
        settingsBinding.settingsFragmentRelativeSetting3.setOnClickListener(this)
        settingsBinding.settingsFragmentRelativeSetting4.setOnClickListener(this)
        settingsBinding.settingsFragmentImgBack.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsBinding = FragmentSettingsBinding.inflate(inflater)
        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
       p0?.let {
           when (it.id){
               R.id.settings_fragment_relativeSetting1 -> goToNewsFontPage()
               R.id.settings_fragment_relativeSetting2 -> goToContactPage()
               R.id.settings_fragment_relativeSetting3 -> goToPrivacyAndTermsPage(true)
               R.id.settings_fragment_relativeSetting4 -> goToPrivacyAndTermsPage(false)
               R.id.settings_fragment_imgBack -> backToPage()
           }
       }
    }

    private fun goToNewsFontPage(){
        navDirections = SettingsFragmentDirections.actionSettingsFragmentToFontNewsFragment()
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToContactPage(){
        navDirections = SettingsFragmentDirections.actionSettingsFragmentToContactFragment()
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToPrivacyAndTermsPage(fromPrivacy: Boolean){
        navDirections = SettingsFragmentDirections.actionSettingsFragmentToPrivacyAndTermsFragment(fromPrivacy)
        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun backToPage(){
        Navigation.findNavController(v).popBackStack()
    }
}