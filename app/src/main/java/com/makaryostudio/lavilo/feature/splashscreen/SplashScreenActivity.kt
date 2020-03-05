package com.makaryostudio.lavilo.feature.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.feature.home.HomeActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(500)
                    startActivity(Intent(baseContext, HomeActivity::class.java))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
