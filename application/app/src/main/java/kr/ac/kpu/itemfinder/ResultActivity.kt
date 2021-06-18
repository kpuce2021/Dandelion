package kr.ac.kpu.itemfinder

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_result.*
import java.io.File
import java.io.IOException


class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val options = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

        product_img_imageview.contentDescription = intent.getStringExtra("product_name")
        Glide.with(this).load(File(cacheDir,"resize.jpg")).apply(options).into(product_img_imageview)

        product_name_tv.text = "${intent.getStringExtra("product_name")}\n${intent.getStringExtra("product_confidence")}"

        result_activity_cls_btn.setOnClickListener {
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
