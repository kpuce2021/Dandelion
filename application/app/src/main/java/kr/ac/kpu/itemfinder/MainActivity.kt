package kr.ac.kpu.itemfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var localModel: LocalModel
    private lateinit var customImageLabelerOptions: CustomImageLabelerOptions
    private lateinit var labeler: ImageLabeler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
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

        /////////////////////////////////////////////////////////////
        /*
        // Firebase Storage
        // Storage Field Initialize
        storage = FirebaseStorage.getInstance()
        // Create a storage reference from our app
        storageRef = storage.reference
         */
        ////////////////////////////////////////////////////////////

        // ML Kit
        localModel = LocalModel.Builder().setAssetFilePath("model_old0220.tflite").build()
                // or .setAbsoluteFilePath(absolute file path to model file)
                // or .setUri(URI to model file)

        customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
                .setConfidenceThreshold(0.5f)
                .setMaxResultCount(2)
                .build()
        labeler = ImageLabeling.getClient(customImageLabelerOptions)

    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        /*
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg")
         */
        val photoFile = File(outputDirectory, "temp.jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                // 성공한 경우 업로드 진행
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    //Log.d(TAG, msg)

                    /*
                    // [START upload_file]
                    val serverRef = storageRef.child("images/${savedUri.lastPathSegment}")
                    val uploadTask = serverRef.putFile(savedUri)

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener {
                        // Handle unsuccessful uploads
                        Toast.makeText(baseContext, "Photo Upload Fail\n${it.message}", Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener { taskSnapshot ->
                        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                        Toast.makeText(baseContext, "Photo Upload Success", Toast.LENGTH_SHORT).show()
                    }
                    // [END upload_file]
                    */

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
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    /*
    private class YourImageAnalyzer : ImageAnalysis.Analyzer {

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Pass image to an ML Kit Vision API
                // ...
            }
        }
    }

    private fun imageFromMediaImage(mediaImage: Image, rotation: Int) {
        // [START image_from_media_image]
        val image = InputImage.fromMediaImage(mediaImage, rotation)
        // [END image_from_media_image]
    }
    */
}