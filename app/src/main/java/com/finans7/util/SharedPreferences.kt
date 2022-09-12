package com.finans7.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences {
    private var sharedPref: SharedPreferences
    private var sharedEdit: SharedPreferences.Editor
    private var mContext: Context

    constructor(mContext: Context){
        this.mContext = mContext
        sharedPref = mContext.getSharedPreferences("NewsFont", Context.MODE_PRIVATE)
        sharedEdit = sharedPref.edit()
    }

    fun saveFontSize(fontSize: Int){
        sharedEdit.putInt("FontSize", fontSize)
        sharedEdit.commit()
    }

    fun saveUserTopic(){
        sharedEdit.putBoolean("UserTopic", true)
        sharedEdit.commit()
    }

    fun getFontSize() = sharedPref.getInt("FontSize", 100)

    fun getUserTopic() = sharedPref.getBoolean("UserTopic", false)
}