package com.hr.hub

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity:AppCompatActivity() {
    private val Splash_screen_duration:Long=1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
          val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        },Splash_screen_duration)


    }
}