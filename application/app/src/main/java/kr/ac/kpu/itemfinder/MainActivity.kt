package kr.ac.kpu.itemfinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var localModel: LocalModel
    private lateinit var customImageLabelerOptions: CustomImageLabelerOptions
    private lateinit var labeler: ImageLabeler

    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listener for take photo button
        camera_capture_button.setOnClickListener { takePhoto() }

        help_button.setOnClickListener {
            Toast.makeText(baseContext, "help_button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(baseContext, HelpActivity::class.java)
            startActivity(intent)
        }

        //outputDirectory = getOutputDirectory()
        outputDirectory = cacheDir
        cameraExecutor = Executors.newSingleThreadExecutor()

        /*
        // ML Kit
        localModel = LocalModel.Builder().setAssetFilePath("model_old0220.tflite").build()
                // or .setAbsoluteFilePath(absolute file path to model file)
                // or .setUri(URI to model file)

        customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
                .setConfidenceThreshold(0.5f)
                .setMaxResultCount(2)
                .build()
        labeler = ImageLabeling.getClient(customImageLabelerOptions)
         */

        // Retrofit
        initRetrofit()

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(outputDirectory, "temp.jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                // 성공한 경우 업로드 진행
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"

                    /*
                    val image: InputImage
                    try {
                        image = InputImage.fromFilePath(this@MainActivity, savedUri)
                        labeler.process(image)
                                .addOnSuccessListener { labels ->
                                    // Task completed successfully
                                    if(labels.isEmpty()) {
                                        Toast.makeText(baseContext, "labeler.process Fail", Toast.LENGTH_SHORT).show()
                                    }else {
                                        Toast.makeText(baseContext, "labeler.process Success\n${labels[0].text}\n${labels[0].confidence}", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(baseContext, ResultActivity::class.java)
                                        intent.putExtra("product_name", labels[0].text)
                                        startActivity(intent)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    // Task failed with an exception
                                    Toast.makeText(baseContext, "labeler.process Fail\n${e.message}", Toast.LENGTH_SHORT).show()
                                }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                     */

                    val requestBody: RequestBody = RequestBody.create(
                        MediaType.parse("image/*"),
                        photoFile
                    )
                    val body: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "image",
                        "temp.jpg",
                        requestBody
                    )
                    getProductInfo(retrofitService, body)
                }
            })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun getProductInfo(service: RetrofitService, body: MultipartBody.Part) {
        service.productPredict(body).enqueue(object : Callback<ProductVO> {
            override fun onFailure(call: Call<ProductVO>, t: Throwable) {
                Toast.makeText(baseContext, "getProductInfo_onFailure\n$t", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<ProductVO>, response: Response<ProductVO>) {
                Toast.makeText(
                    baseContext,
                    "getProductInfo_onResponse\n${response.body()!!}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun resize(imgUri: Uri): Bitmap {
        val bitmap = Images.Media.getBitmap(contentResolver, imgUri)
        val height = bitmap.height
        val width = bitmap.width
        val resizedBitmap : Bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        Toast.makeText(baseContext, "width = ${resizedBitmap.width}\nheight = ${resizedBitmap.height}", Toast.LENGTH_SHORT)
            .show()
        return resizedBitmap
    }
}