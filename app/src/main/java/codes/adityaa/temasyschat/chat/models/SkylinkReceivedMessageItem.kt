package codes.adityaa.temasyschat.chat.models

data class SkylinkReceivedMessageItem(
    val emailID: String ? ="",
    val emailId: String? = "",
    val message: String,
    val senderId: String,
    val timeStamp: Long,
    val userID: String = "",
    val userId: String= ""
)