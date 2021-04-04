package kr.ac.kpu.itemfinder

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

private const val IMMERSIVE_FLAG_TIMEOUT = 500L

class MainActivity : AppCompatActivity() {
    private lateinit var container: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.fragment_container)

        val sPreferences: SharedPreferences = getSharedPreferences("help_flag", MODE_PRIVATE);
        if(sPreferences.getInt("help", 0) == 0){
            val intent = Intent(this@MainActivity, HelpActivity::class.java)
            startActivity(intent)
        }
    }
}