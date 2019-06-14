import com.google.gson.annotations.SerializedName

object Model {
    data class ApiResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("fullname") val fullname: String,
        @SerializedName("url") var url: String,
        @SerializedName("foto") val foto: String?,
        @SerializedName("important") val important: Int,
        @SerializedName("feast") val feast: String?
    )
}