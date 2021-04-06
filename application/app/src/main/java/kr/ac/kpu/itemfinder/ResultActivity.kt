package kr.ac.kpu.itemfinder

import android.os.Bundle
import android.widget.Toast
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

        product_name_button.text = intent.getStringExtra("product_name")+"\n"+intent.getStringExtra("product_confidence")
        val options = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

        val img = File(cacheDir,"resize.jpg")
        var exif : ExifInterface? = null
        try {
            exif = ExifInterface(File(cacheDir,"temp.jpg"))
            Toast.makeText(baseContext, "rotationDegrees = ${exif.rotationDegrees}", Toast.LENGTH_SHORT).show()
            when(exif.rotationDegrees) {
                ExifInterface.ORIENTATION_ROTATE_90 -> Glide.with(this).load(img).transform(RotateTransform(baseContext, 90f)).apply(options).into(product_img_imageview)
                ExifInterface.ORIENTATION_ROTATE_180 -> Glide.with(this).load(img).transform(RotateTransform(baseContext, 180f)).apply(options).into(product_img_imageview)
                ExifInterface.ORIENTATION_ROTATE_270 -> Glide.with(this).load(img).transform(RotateTransform(baseContext, 270f)).apply(options).into(product_img_imageview)
                else -> Glide.with(this).load(img).apply(options).into(product_img_imageview)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
