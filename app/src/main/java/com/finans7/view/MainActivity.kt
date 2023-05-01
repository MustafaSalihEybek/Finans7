package com.finans7.view

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.finans7.R
import com.finans7.util.Singleton
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    private lateinit var themeMode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        val nightModeFlags: Int = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                themeMode = "Dark"
                setTheme(R.style.Theme_Dark)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                themeMode = "Light"
                setTheme(R.style.Theme_Light)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                themeMode = "Light"
                setTheme(R.style.Theme_Light)
            }
        }
        Singleton.themeMode = themeMode
//        super.onCreate(savedInstanceState)
        try {
            super.onCreate(null)
        }catch (ex : InstantiationException){

        }
        catch (ex : RuntimeException){

        }

        setContentView(R.layout.activity_main)

        Singleton.window = window

        if (themeMode.equals("Dark")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
                windowInsetController?.isAppearanceLightStatusBars = true
            }
        }

        MobileAds.initialize(
            this
        ) { }
    }
}