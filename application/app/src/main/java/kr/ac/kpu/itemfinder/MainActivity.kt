package kr.ac.kpu.itemfinder

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
//import com.google.mlkit.common.model.LocalModel
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.label.ImageLabeler
//import com.google.mlkit.vision.label.ImageLabeling
//import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
//import id.zelory.compressor.Compressor
//import id.zelory.compressor.constraint.default
//import id.zelory.compressor.constraint.destination
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Dispatcher
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutorService
import okhttp3.MultipartBody
import okhttp3.RequestBody

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