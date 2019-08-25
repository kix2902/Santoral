package es.kix2902.santoral.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

object Model {
    @Entity(tableName = "saints")
    data class Saint(
        @PrimaryKey @SerializedName("id") val id: Int,
        @SerializedName("fullname") val fullname: String,
        @SerializedName("name") val name: String,
        @SerializedName("date") var date: String,
        @SerializedName("important") val important: Int,
        @SerializedName("url") var url: String,
        @SerializedName("names") val names: String
    )

    @Entity(primaryKeys = arrayOf("month", "day"))
    data class QueryInfo(
        val month: Int,
        val day: Int,
        val dateSaved: Date
    )
}