package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.MainAsteroidAdapter

/* When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
*/
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as MainAsteroidAdapter
    adapter.submitList(data)
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
//        Picasso.get().load(R.drawable.ic_status_potentially_hazardous).into(imageView)
    } else {
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(R.drawable.ic_status_normal)
//        Picasso.get().load(R.drawable.ic_status_normal).into(imageView)
    }
}

@BindingAdapter("pictureOfDay")
fun bindPictureOfToday(imageView: ImageView, pictureOfDay: PictureOfDay?){

    when(pictureOfDay?.mediaType){
        Constants.MEDIA_TYPE -> Picasso.get().load(pictureOfDay.url).into(imageView)
        else -> Picasso.get().load(R.drawable.asteroid_safe).into(imageView)
    }

}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }

}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
