package es.kix2902.santoral.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

object Model {
    @Entity(tableName = "saints")
    data class Saint(
        @PrimaryKey @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("fullname") val fullname: String,
        @SerializedName("url") var url: String,
        @SerializedName("foto") val foto: String?,
        @SerializedName("important") val important: Int,
        @SerializedName("feast") var feast: String?
    )

    @Entity(primaryKeys = arrayOf("month", "day"))
    data class QueryInfo(
        val month: Int,
        val day: Int,
        val language: String,
        val dateSaved: Date
    )
}