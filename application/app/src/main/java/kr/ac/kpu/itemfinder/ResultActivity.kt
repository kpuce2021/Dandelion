package kr.ac.kpu.itemfinder

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_result.*
import java.io.File
import kotlin.math.roundToInt


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val options = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
        Glide.with(this).load(File(cacheDir,"resize.jpg")).apply(options).into(product_img_imageview)

        val product_confidence = intent.getStringExtra("product_confidence")!!.toDouble() * 100

        product_name_tv.text = "${intent.getStringExtra("product_name")}\n정확도 "+product_confidence.roundToInt()+"%"
        product_img_imageview.contentDescription = intent.getStringExtra("product_name")+" 정확도 "+product_confidence.roundToInt()+"%"

        /*
        when {
            product_confidence <= 20 -> {
                product_name_tv.text = "${intent.getStringExtra("product_name")}\n정확도 낮음"
                product_img_imageview.contentDescription = intent.getStringExtra("product_name")+" 정확도 낮음"
            }
            product_confidence <= 70 -> {
                product_name_tv.text = "${intent.getStringExtra("product_name")}\n정확도 보통"
                product_img_imageview.contentDescription = intent.getStringExtra("product_name")+" 정확도 보통"
            }
            else -> {
                product_name_tv.text = "${intent.getStringExtra("product_name")}\n정확도 높음"
                product_img_imageview.contentDescription = intent.getStringExtra("product_name")+" 정확도 높음"
            }
        }
        */

        result_layout.setOnClickListener {
            finish()
        }

        val vibrator : Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500);
        }
    }
}
