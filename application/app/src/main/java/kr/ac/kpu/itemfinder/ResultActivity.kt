package kr.ac.kpu.itemfinder

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
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
        try {
            when(ExifInterface(File(cacheDir,"temp.jpg")).rotationDegrees) {
                90 -> options.transform(RotateTransform( 90f))
                180 -> options.transform(RotateTransform( 180f))
                270 -> options.transform(RotateTransform( 270f))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        product_img_imageview.contentDescription = intent.getStringExtra("product_name")
        Glide.with(this).load(File(cacheDir,"resize.jpg")).apply(options).into(product_img_imageview)

        product_name_button.text = "${intent.getStringExtra("product_name")}\n${intent.getStringExtra("product_confidence")}"
        product_name_button.setOnLongClickListener {
            finish()
            true
        }

        val vibrator : Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(1000);
        }
    }
}
