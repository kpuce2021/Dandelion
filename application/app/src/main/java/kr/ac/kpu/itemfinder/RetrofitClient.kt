package kr.ac.kpu.itemfinder

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private var instance: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null
    private val gson = GsonBuilder().setLenient().create()

    // Server Address
    private const val BASE_URL = "http://59.14.252.2:5000/"

    // SingleTon
    fun getInstance(): Retrofit {
        if (instance == null) {
            okHttpClient = OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .readTimeout(3, TimeUnit.SECONDS)
                    .writeTimeout(3, TimeUnit.SECONDS)
                    .build()
            instance = Retrofit.Builder()
                    .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return instance!!
    }

    fun getProductInfo(context: Context, service: RetrofitService, body: MultipartBody.Part) {
        service.productPredict(body).enqueue(object : Callback<List<ProductVO2>> {
            val vibrator : Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            val pattern = longArrayOf(0, 100, 50, 100)

            override fun onFailure(call: Call<List<ProductVO2>>, t: Throwable) {
                Toast.makeText(context, "getProductInfo_onFailure\n$t", Toast.LENGTH_SHORT).show()
                Log.e("getProduct", t.toString())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
                } else {
                    vibrator.vibrate(pattern, -1)
                }
            }

            override fun onResponse(call: Call<List<ProductVO2>>, response: Response<List<ProductVO2>>) {
                if (!response.body().isNullOrEmpty()) {
                    Log.i("getProductInfo", "${response.body()?.get(0)?.getName().toString()}, ${response.body()?.get(0)?.getConfidence().toString()}")
                    val intent = Intent(context, ResultActivity::class.java)
                    intent.putExtra("product_name", response.body()?.get(0)?.getName().toString())
                    intent.putExtra("product_confidence", response.body()?.get(0)?.getConfidence().toString())
                    intent.flags = FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(context, intent, null)
                } else {
                    Log.i("getProductInfo", "null")
                    Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
                    } else {
                        vibrator.vibrate(pattern, -1)
                    }
                }
            }
        })
    }
}