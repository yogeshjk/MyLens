package com.yogi.mylens.loginProcess

import android.content.Context
import android.content.SharedPreferences


object SharedPref {

    private const val NAME = "MyShot"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }




    fun putData(key: String, value: String){
        preferences.edit().putString(key,value).apply()
    }

    fun getData(key: String ): String? {
        return preferences.getString(key, "null" )
    }

}