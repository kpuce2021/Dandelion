package kr.ac.kpu.itemfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        product_name_textview.text = intent.getStringExtra("product_name")+"\n"+intent.getStringExtra("product_confidence")
    }
}