package codes.adityaa.temasyschat.chat.models

import com.google.gson.annotations.SerializedName

data class SkylinkServerMessage(
    @SerializedName("message")
    val message: String,

    @SerializedName("emailID")
    val emailId: String,

    @SerializedName("userID")
    val userId: String,
)