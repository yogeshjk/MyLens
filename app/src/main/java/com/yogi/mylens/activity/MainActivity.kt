package com.yogi.mylens.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.yogi.mylens.R
import com.yogi.mylens.loginProcess.SharedConst
import com.yogi.mylens.loginProcess.SharedPref

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SharedPref.init(this)

        val isUserLoggedIn = SharedPref.getBoolean(SharedConst.IS_USER_LOGGED_IN)

        val destinationActivity = if (isUserLoggedIn) {
            PhotographyActivity::class.java

        } else {
            GetStartActivity::class.java
        }
        val intent = Intent(this, destinationActivity)
        startActivity(intent)

        finish()
    }
}
