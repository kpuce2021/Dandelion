package kr.ac.kpu.itemfinder

import android.content.Context
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception

object RetrofitClient {
    private var instance: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()
    // 서버 주소
    private const val BASE_URL = "http://59.14.252.2:5000/"

    // SingleTon
    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return instance!!
    }

    fun getProductInfo(context: Context, service: RetrofitService, body: MultipartBody.Part) {
        service.productPredict(body).enqueue(object : Callback<ProductVO> {
            override fun onFailure(call: Call<ProductVO>, t: Throwable) {
                Toast.makeText(context, "getProductInfo_onFailure\n$t", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProductVO>, response: Response<ProductVO>) {
                Toast.makeText(context, "getProductInfo_onResponse\n${response.body()!!}", Toast.LENGTH_SHORT).show()
                try {
                    File(context.cacheDir.absolutePath+"resize_temp.jpg").delete()
                }catch (e: Exception) {
                }
            }
        })
    }
}