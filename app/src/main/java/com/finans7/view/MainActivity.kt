package com.finans7.view

import android.app.Activity
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.finans7.R
import com.finans7.util.Singleton

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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Singleton.window = window
    }
}