package kr.ac.kpu.itemfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_result.*
import java.io.File

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        product_name_textview.text = intent.getStringExtra("product_name")+"\n"+intent.getStringExtra("product_confidence")
        Glide.with(this).load(File(cacheDir, "resize.jpg")).diskCacheStrategy(DiskCacheStrategy.NONE).into(product_img_imageview)
    }
}