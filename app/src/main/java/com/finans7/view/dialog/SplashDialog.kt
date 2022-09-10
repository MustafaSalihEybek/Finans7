package com.finans7.view.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.ActionBar
import com.finans7.databinding.SplashDialogBinding
import com.finans7.util.Singleton

class SplashDialog(val mContext: Context) : Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
    private lateinit var v: SplashDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        v = SplashDialogBinding.inflate(LayoutInflater.from(mContext))
        setContentView(v.root)

        window?.let {
            it.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        }
    }
}