package codes.adityaa.temasyschat.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.recyclerview.widget.RecyclerView
import codes.adityaa.temasyschat.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.world.suuz.util.PercentageLinearLayoutManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun View.setMarginEnd(margin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(marginStart, marginTop, margin, marginBottom)
    layoutParams = params
}

fun View.setMarginLeft(margin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(margin.dp, marginTop, marginRight, marginBottom)
    layoutParams = params
}

fun Long.formatTimeLeft(): String {
    var seconds = this / 1000
    val minutes = seconds / 60
    seconds %= 60
    return "%d:%02d".format(minutes, seconds)
}

fun View.setMargins(
    marginStart: Int = this.marginStart,
    marginTop: Int = this.marginLeft,
    marginEnd: Int = this.marginEnd,
    marginBottom: Int = this.marginBottom
) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(marginStart.dp, marginTop.dp, marginEnd.dp, marginBottom.dp)
    layoutParams = params
}

fun View.setWidthBasedOnPercentage(percentage: Float, parentView: View) {
    val layoutParams = layoutParams
    layoutParams.width = (parentView.width * percentage).toInt()
    setLayoutParams(layoutParams)
}

fun RecyclerView.setPercentageLayoutManager(percentage: Float) {
    layoutManager = PercentageLinearLayoutManager(context, percentage)
}

fun Date.formatDate(pattern: String = "dd-MM-yyyy"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

private fun Context.checkLocationPermissions(): Boolean {
    var isLocationPermissionGiven = checkLocationPermission()
    if (!isLocationPermissionGiven) return false
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
        isLocationPermissionGiven = checkBackgroundLocationPermission()
    return isLocationPermissionGiven
}

fun Context.checkLocationPermission(): Boolean {
    val isFineLocationGranted =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
    val isCoarseLocationGranted =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    return isFineLocationGranted == PackageManager.PERMISSION_GRANTED && isCoarseLocationGranted == PackageManager.PERMISSION_GRANTED
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun Context.checkBackgroundLocationPermission() =
    ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

fun Activity.goToMainActivity(finishOff: Boolean) {
    Intent(this, MainActivity::class.java).also {
        startActivity(it)
        if (finishOff) finish()
    }
}


fun Boolean.toInt() = if (this) 1 else 0

fun Context.convertFileToMultipart(uri: Uri, name: String): MultipartBody.Part {
    val stream = contentResolver.openInputStream(uri)
//    val file = FileUtils.getPathFromURI(this, uri).toString()
    val byteBuff = ByteArrayOutputStream()
    val buffSize = 1024
    val buff = ByteArray(buffSize)
    var len: Int
    while (stream!!.read(buff).also { len = it } != -1) {
        byteBuff.write(buff, 0, len)
    }
    val byteArray = byteBuff.toByteArray()
    val requestFile =
        RequestBody.create(contentResolver.getType(uri)?.toMediaTypeOrNull(), byteArray)

    return MultipartBody.Part.createFormData(
        name,
        "SUUZ" + Constants.getCurrentDateTime() + ".jpg",
        requestFile
    )
}

fun View.visibleOrGone(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun EditText.showKeyboard() {
    val inputMm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMm.showSoftInput(this, SHOW_IMPLICIT)
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun SearchView.showKeyboard() {
    val inputMm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMm.showSoftInput(this, SHOW_IMPLICIT)
}





fun Context.showAlertDialog(
    title: String,
    description: String,
    positiveButtonText: String,
    negativeButtonText: String = "Cancel",
    onCancelClick: (() -> Unit) = { },
    onConfirmClick: () -> Unit
) {
    MaterialAlertDialogBuilder(this).apply {
        setTitle(title)
        setMessage(description)
        setPositiveButton(positiveButtonText) { _, _ -> onConfirmClick() }
        setNegativeButton(negativeButtonText) { _, _ -> onCancelClick() }
    }.show()
}
