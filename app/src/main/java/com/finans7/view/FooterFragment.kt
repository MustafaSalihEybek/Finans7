package com.finans7.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.finans7.R
import com.finans7.databinding.FragmentFooterBinding
import com.finans7.util.AppUtil

class FooterFragment(val fromMain: Boolean) : Fragment(), View.OnClickListener {
    private lateinit var v: View
    private var footerBinding: FragmentFooterBinding? = null
    private lateinit var navDirections: NavDirections

    private fun init(){
        footerBinding?.let {
            it.footerFragmentImgInstagram.setOnClickListener(this)
            it.footerFragmentImgTwitter.setOnClickListener(this)
            it.footerFragmentTxtContact.setOnClickListener(this)
            it.footerFragmentTxtPrivacy.setOnClickListener(this)
            it.footerFragmentTxtTerms.setOnClickListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        footerBinding = FragmentFooterBinding.inflate(inflater)
        return footerBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id){
                R.id.footer_fragment_imgInstagram -> AppUtil.goToInstagramPage(v.context)
                R.id.footer_fragment_imgTwitter -> AppUtil.goToTwitterPage(v.context)
                R.id.footer_fragment_txtContact -> goToContactPage()
                R.id.footer_fragment_txtPrivacy -> goToPrivacyAndTermsPage(true)
                R.id.footer_fragment_txtTerms -> goToPrivacyAndTermsPage(false)
            }
        }
    }

    private fun goToContactPage(){
        if (fromMain)
            navDirections = MainFragmentDirections.actionMainFragmentToContactFragment()
        else
            navDirections = NewsFragmentDirections.actionNewsFragmentToContactFragment()

        Navigation.findNavController(v).navigate(navDirections)
    }

    private fun goToPrivacyAndTermsPage(fromPrivacy: Boolean){
        if (fromMain)
            navDirections = MainFragmentDirections.actionMainFragmentToPrivacyAndTermsFragment(fromPrivacy)
        else
            navDirections = NewsFragmentDirections.actionNewsFragmentToPrivacyAndTermsFragment(fromPrivacy)

        Navigation.findNavController(v).navigate(navDirections)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        footerBinding = null
    }
}