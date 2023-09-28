package com.yogi.mylens.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yogi.mylens.R

class GetStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_start)

        //supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, GetReadyFragment()).commit()
    }
}



