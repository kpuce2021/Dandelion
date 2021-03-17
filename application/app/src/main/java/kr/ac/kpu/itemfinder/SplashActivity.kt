package kr.ac.kpu.itemfinder

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val sPreferences: SharedPreferences = getSharedPreferences("help_flag", MODE_PRIVATE);
            val sPreferencesEditor: SharedPreferences.Editor = sPreferences.edit()
            lateinit var intent: Intent
            if(sPreferences.getInt("help", 0) == 0){
                sPreferencesEditor.putInt("help", 1)
                sPreferencesEditor.apply()
                intent = Intent(this@SplashActivity, HelpActivity::class.java)
            }else {
                intent = Intent(this@SplashActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 1500L)
    }
}