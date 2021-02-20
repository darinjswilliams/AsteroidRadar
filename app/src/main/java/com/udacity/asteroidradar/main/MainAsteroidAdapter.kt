package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidViewItemBinding

class MainAsteroidAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Asteroid, MainAsteroidAdapter.AsteroidViewHolder>(DiffCallback) {


    class AsteroidViewHolder(private var binding: AsteroidViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroidsProperty = asteroid
            binding.executePendingBindings()
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroidProperty = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(asteroidProperty)
        }
        holder.bind(asteroidProperty)
    }

    companion object DiffCallback : ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return newItem.id == oldItem.id
        }

    }

    // Create a name lamda
    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}
