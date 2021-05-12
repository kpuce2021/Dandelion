package kr.ac.kpu.itemfinder

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ProductVO2 {
    @SerializedName("xmin")
    @Expose
    private var xmin: Double? = null

    @SerializedName("ymin")
    @Expose
    private var ymin: Double? = null

    @SerializedName("xmax")
    @Expose
    private var xmax: Double? = null

    @SerializedName("ymax")
    @Expose
    private var ymax: Double? = null

    @SerializedName("confidence")
    @Expose
    private var confidence: Double? = null
    /*
    @SerializedName("class")
    @Expose
    private var _class: Int? = null
    */
    @SerializedName("name")
    @Expose
    private var name: String? = null

    fun getXmin(): Double? {
        return xmin
    }

    fun setXmin(xmin: Double?) {
        this.xmin = xmin
    }

    fun getYmin(): Double? {
        return ymin
    }

    fun setYmin(ymin: Double?) {
        this.ymin = ymin
    }

    fun getXmax(): Double? {
        return xmax
    }

    fun setXmax(xmax: Double?) {
        this.xmax = xmax
    }

    fun getYmax(): Double? {
        return ymax
    }

    fun setYmax(ymax: Double?) {
        this.ymax = ymax
    }

    fun getConfidence(): Double? {
        return confidence
    }

    fun setConfidence(confidence: Double?) {
        this.confidence = confidence
    }
    /*
    fun getClass_(): Int? {
        return _class
    }

    fun setClass_(_class: Int?) {
        this._class = _class
    }
    */
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

}