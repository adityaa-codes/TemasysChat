package codes.adityaa.temasyschat.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import codes.adityaa.temasyschat.R
import com.bumptech.glide.Glide

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(url: String?) {
    val newUrl = url?:""
        Glide.with(context).load(newUrl).error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .fallback(R.drawable.ic_placeholder)
            .into(this)


    if (url == null || url == "" || url == " ") {
        Glide.with(context).load(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder)
            .fallback(R.drawable.ic_placeholder)
            .into(this)
    }
}



@BindingAdapter("setImageRes")
fun ImageView.setImageRes(image: Int) {
    setImageResource(image)
}

@BindingAdapter("setDrawableStartFromInt")
fun TextView.setDrawableStart(image: Int) {
    setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0)
    compoundDrawablePadding = 16
}

@BindingAdapter("setDrawableTopFromInt")
fun TextView.setDrawableTop(image: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, image, 0, 0)
    compoundDrawablePadding = 16
}
