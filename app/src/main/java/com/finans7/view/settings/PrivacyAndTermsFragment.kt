package com.finans7.view.settings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.finans7.R
import com.finans7.databinding.FragmentPrivacyAndTermsBinding
import com.finans7.util.Singleton

class PrivacyAndTermsFragment : Fragment() {
    private lateinit var v: View
    private lateinit var privacyAndTermsBinding: FragmentPrivacyAndTermsBinding

    private lateinit var webViewUrl: String
    private var fromPolicy: Boolean = false

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(){
        arguments?.let {
            fromPolicy = PrivacyAndTermsFragmentArgs.fromBundle(it).fromPrivacy

            privacyAndTermsBinding.privacyAndTermsFragmentWebView.settings.javaScriptEnabled = true
            privacyAndTermsBinding.privacyAndTermsFragmentWebView.settings.setGeolocationEnabled(true)

            if (fromPolicy) {
                privacyAndTermsBinding.privacyAndTermsFragmentTxtTitle.text = "Gizlilik Sözleşmesi"
                webViewUrl = Singleton.PRIVACY_POLICY_URL
            }
            else {
                privacyAndTermsBinding.privacyAndTermsFragmentTxtTitle.text = "Künye"
                webViewUrl = Singleton.TERMS_OF_SERVICE
            }

            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK) && Singleton.themeMode.equals("Dark")) {
                WebSettingsCompat.setForceDark(privacyAndTermsBinding.privacyAndTermsFragmentWebView.settings, WebSettingsCompat.FORCE_DARK_ON);
            }

            privacyAndTermsBinding.privacyAndTermsFragmentWebView.loadUrl(webViewUrl)
        }

        privacyAndTermsBinding.privacyAndTermsFragmentImgBack.setOnClickListener {
            Navigation.findNavController(v).popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        privacyAndTermsBinding = FragmentPrivacyAndTermsBinding.inflate(inflater)
        return privacyAndTermsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        v = view
        init()
    }
}