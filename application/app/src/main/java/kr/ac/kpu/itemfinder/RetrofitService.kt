package kr.ac.kpu.itemfinder

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    //@Headers("accept: application/json", "content-type: application/json")
    @Multipart
    @POST("API")
    fun productPredict(@Part imageFile : MultipartBody.Part) : Call<List<ProductVO2>>
}