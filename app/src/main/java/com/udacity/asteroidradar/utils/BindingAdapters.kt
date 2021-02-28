package com.udacity.asteroidradar.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.main.AsteroidApiStatus
import com.udacity.asteroidradar.main.MainAsteroidAdapter
import com.udacity.asteroidradar.main.MainFragment
import com.udacity.asteroidradar.main.MainViewModel
import timber.log.Timber


/* When there is no Asteroid property data (data is null), hide the [RecyclerView], otherwise show it.
*/
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    Timber.i("bindRecyclerViewCalled")
    val adapter = recyclerView.adapter as MainAsteroidAdapter
    adapter.submitList(data)
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("pictureOfDay")
fun bindPictureOfToday(imageView: ImageView, pictureOfDay: PictureOfDay?) {

    when (pictureOfDay?.mediaType) {
        Constants.MEDIA_TYPE -> Picasso.get().load(pictureOfDay.url).fit().centerCrop().into(imageView)
        else -> Picasso.get().load(R.drawable.asteroid_safe).into(imageView)
    }

}

//Dynamically set contentDescription for image of day
@BindingAdapter("accessibilityPictureOfTheDay")
fun ImageView.bindAccessibilityPictureOfTheDay(value: String?) {

    contentDescription = when (value) {
        null -> context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
        else -> context.getString( R.string.nasa_picture_of_day_content_description_format, value)
    }
}


@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.visibility = View.VISIBLE
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

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

