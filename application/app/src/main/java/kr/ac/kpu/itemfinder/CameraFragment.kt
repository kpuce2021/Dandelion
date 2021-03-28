package kr.ac.kpu.itemfinder

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import kr.ac.kpu.itemfinder.RetrofitClient.getProductInfo
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    // Retrofit 관련 변수
    private lateinit var retrofit : Retrofit
    private lateinit var retrofitService : RetrofitService

    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView
    private lateinit var outputDirectory: File

    private var displayId: Int = -1
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRetrofit()
    }

    override fun onResume() {
        super.onResume()
        if(!PermissionFragment.hasPermissions(requireContext())) {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
                ActionOnlyNavDirections(R.id.action_permissions_to_camera)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Shut down our background executor
        cameraExecutor.shutdown()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container = view as ConstraintLayout
        viewFinder = container.findViewById(R.id.view_finder)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Determine the output directory
        outputDirectory = requireContext().cacheDir

        // Wait for the views to be properly laid out
        viewFinder.post {

            // Keep track of the display in which this view is attached
            displayId = viewFinder.display.displayId

            // Build UI controls
            updateCameraUi()

            // Set up the camera and its use cases
            setUpCamera()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Redraw the camera UI controls
        updateCameraUi()
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            // CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // Build and bind the camera use cases
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases() {
        // CameraProvider
        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        // Preview
        preview = Preview.Builder().build()

        // ImageCapture
        imageCapture = ImageCapture.Builder().setTargetResolution(Size(480, 640))
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture)

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun updateCameraUi() {
        // Remove previous UI if any
        container.findViewById<ConstraintLayout>(R.id.camera_ui_container)?.let {
            container.removeView(it)
        }

        // Inflate a new view containing all UI for controlling the camera
        val controls = View.inflate(requireContext(), R.layout.camera_ui_container, container)

        // Listener for button used to capture photo
        controls.findViewById<Button>(R.id.camera_capture_button).setOnClickListener {
            // Get a stable reference of the modifiable image capture use case
            imageCapture?.let { imageCapture ->
                // Create output file to hold the image
                val photoFile = File(outputDirectory, "temp.jpg")

                // Create output options object which contains file + metadata
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                        Log.d(TAG, "Photo capture succeeded: $savedUri")

                        val bitmap = resize(requireContext(), savedUri, 500)
                        //val time = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA).format(System.currentTimeMillis())
                        val resizeImage = File(requireContext().cacheDir, "resize.jpg")
                        resizeImage.createNewFile()
                        val fileOutputStream = FileOutputStream(resizeImage)
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                        fileOutputStream.flush()
                        fileOutputStream.close()

                        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), resizeImage)
                        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", "resize.jpg", requestBody)
                        getProductInfo(requireContext(), retrofitService, body)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                    }

                })
            }
        }
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    private fun resize(context: Context, uri: Uri, resize: Int): Bitmap? {
        var resizeBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        try {
            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options) // 1번
            var width = options.outWidth
            var height = options.outHeight
            var samplesize = 1
            while (true) { //2번
                if (width / 2 < resize || height / 2 < resize) break
                width /= 2
                height /= 2
                samplesize *= 2
            }
            options.inSampleSize = samplesize
            val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options) //3번
            resizeBitmap = bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return resizeBitmap
    }

    companion object {
        private const val TAG = "CameraXBasic"
    }
}