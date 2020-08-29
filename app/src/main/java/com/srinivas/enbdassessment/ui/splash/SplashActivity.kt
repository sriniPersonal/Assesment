package com.srinivas.enbdassessment.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.srinivas.enbdassessment.R
import com.srinivas.enbdassessment.ui.PixaBayActivity

class SplashActivity : AppCompatActivity() {
    private var SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable { // This method will be executed once the timer is over
            // Start your app main activity
            val intent = Intent(this@SplashActivity, PixaBayActivity::class.java)
            startActivity(intent)

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)

    }
}