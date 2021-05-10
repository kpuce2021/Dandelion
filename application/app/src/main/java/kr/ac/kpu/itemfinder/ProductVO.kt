package kr.ac.kpu.itemfinder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ProductVO(val data: Data)
data class Data(val content: List<Content>)
data class Content(
        @SerializedName("xmin")
        @Expose
        private var xmin: String? = null,

        @SerializedName("ymin")
    @Expose
    private var ymin: String? = null,

    @SerializedName("xmax")
    @Expose
    private var xmax: String? = null,

    @SerializedName("ymax")
    @Expose
    private var ymax: String? = null,

    @SerializedName("confidence")
    @Expose
    private var confidence: String? = null,


    @SerializedName("class")
    @Expose
    private var `class`: String? = null,


    @SerializedName("name")
    @Expose
    private var name: String? = null
)
