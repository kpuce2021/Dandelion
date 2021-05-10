package kr.ac.kpu.itemfinder

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.Exception
import kotlin.math.log

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
        service.productPredict(body).enqueue(object : Callback<List<ProductVO>> {


            override fun onFailure(call: Call<List<ProductVO>>, t: Throwable) {
                Toast.makeText(context, "getProductInfo_onFailure\n$t", Toast.LENGTH_SHORT).show()

            }
            override fun onResponse(call: Call<List<ProductVO>>, response: Response<List<ProductVO>>) {
                if(response.isSuccessful){
                    var items : List<ProductVO>? =  response.body()

                    val intent = Intent(context, ResultActivity::class.java)
                    Log.i("getProductInfo", response.body().toString())
                    intent.putExtra("product_name", items.get(0).data.toString())
                    intent.putExtra("product_confidence", items?.get(0)?.toString())
                    intent.flags = FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(context, intent, null)

                }

            }
        })
    }
}