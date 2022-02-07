package codes.adityaa.temasyschat.util.sharedPreference

import android.content.SharedPreferences
import codes.adityaa.temasyschat.chat.models.Roomlist
import com.google.gson.Gson
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private val USER_EMAIL = "user_email"
        private val USER_ID = "user_id"
        private val FIRST_NAME = "first_name"
        private val PROFILE_OBJECT = "profile_string"
        private val ROOM_LIST_OBJECT = "room_list_string"
        private val LOCATION_OBJECT = "location_string"
        private val BROWSING_LOCATION_CITY = "browsing_location_city"
        private val BROWSING_LOCATION_COUNTRY = "browsing_location_COUNTRY"
        private val LOGGED_IN = "LoggedIn"
        private val ROOM_ID = "active_room_id"
        private val ROOM_NAME = "active_room_name"
        private val ROOM_TYPE = "active_room_type"
        private val IP_LOCATION = "IPLocation"
        private val USER_DATA = "UserData"
    }

    val userEmail: String?
        get() = sharedPreferences.getString(USER_EMAIL, null)

    val roomId: String?
        get() = sharedPreferences.getString(ROOM_ID, null)

    fun saveUserEmail(email: String) {
        sharedPreferences.edit().putString(USER_EMAIL, email)
            .apply()
    }

    val userId: String
        get() = sharedPreferences.getString(USER_ID, "1")!!

    val chatType: String
        get() = sharedPreferences.getString(ROOM_TYPE, "One")!!

    val roomName: String
        get() = sharedPreferences.getString(ROOM_NAME, "Null")!!

    fun saveUserId(id: String) {
        sharedPreferences.edit().putString(USER_ID, id)
            .apply()
    }

    fun saveRoomId(id: String) {
        sharedPreferences.edit().putString(ROOM_ID, id)
            .apply()
    }


    private fun removeUserId() = sharedPreferences.edit().remove(USER_ID).apply()

    fun isUserLoggedIn() = sharedPreferences.getBoolean(LOGGED_IN, false)

    fun loginUser() = sharedPreferences.edit().putBoolean(LOGGED_IN, true).apply()

    fun logoutUser() = sharedPreferences.edit().remove(LOGGED_IN).apply()


    val firstName: String?
        get() = sharedPreferences.getString(FIRST_NAME, null)

    fun saveFirstName(firstName: String) {
        sharedPreferences.edit().putString(FIRST_NAME, firstName)
            .apply()
    }


    fun saveRoomListObject(roomList: Roomlist) {
        val gson = Gson()

        sharedPreferences.edit().putString(ROOM_LIST_OBJECT, gson.toJson(roomList)).apply()
    }

    fun getRoomListObject(): Roomlist {
        val gson = Gson()
        return gson.fromJson(
            sharedPreferences.getString(ROOM_LIST_OBJECT, " "),
            Roomlist::class.java
        )
    }

    fun saveChatType(type: String) {
        sharedPreferences.edit().putString(ROOM_TYPE, type)
            .apply()
    }

    fun saveRoomName(name: String) {
        sharedPreferences.edit().putString(ROOM_NAME, name)
            .apply()
    }

    fun isIpLocationTaken(): Boolean = sharedPreferences.getBoolean(IP_LOCATION, false)
    fun setIpLocationTaken() {
        sharedPreferences.edit().putBoolean(IP_LOCATION, true).apply()
    }
}
